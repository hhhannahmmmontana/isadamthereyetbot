package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.kslog.common.error
import dev.inmo.kslog.common.logger
import dev.inmo.tgbotapi.bot.exceptions.CommonRequestException
import dev.inmo.tgbotapi.extensions.api.chat.members.getChatMember
import dev.inmo.tgbotapi.extensions.api.edit.reply_markup.editMessageReplyMarkup
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitDataCallbackQuery
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitTextMessage
import dev.inmo.tgbotapi.extensions.utils.administratorChatMemberOrNull
import dev.inmo.tgbotapi.extensions.utils.channelChatOrNull
import dev.inmo.tgbotapi.extensions.utils.chatIdOrThrow
import dev.inmo.tgbotapi.extensions.utils.fromChannelOrNull
import dev.inmo.tgbotapi.extensions.utils.textContentOrNull
import dev.inmo.tgbotapi.extensions.utils.types.buttons.dataButton
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.chat.ChannelChat
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import dev.inmo.tgbotapi.types.queries.callback.DataCallbackQuery
import dev.inmo.tgbotapi.utils.row
import io.github.hhhannahmmmontana.isadamthereyet.domain.BotData
import io.github.hhhannahmmmontana.isadamthereyet.domain.UserData
import io.github.hhhannahmmmontana.isadamthereyet.states.exceptions.CancelledStateException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.merge

class AddChannelState(
    override val context: ChatIdentifier,
    override val userData: UserData
) : BotState() {
    override val icon: String = "➕"

    override val viewName: String = "Добавить канал"

    override val className: String = AddChannelState::class.simpleName!!

    override suspend operator fun invoke(botData: BotData): BotState {
        val cancelState = CancelState { StartState(context, userData) }

        try {
            botData.sendTextMessage(
                context,
                "Перешли сообщение из нужного канала",
                replyMarkup = replyKeyboard {
                    row {
                        simpleButton(cancelState.nameWithIcon)
                    }
                }
            )

            val channelChat = getChannelChat(botData, cancelState)
            checkPermissions(botData, channelChat, cancelState)

            checkOrConfirm(botData, cancelState, channelChat)
            return StartState(context, userData)
        } catch (_: CancelledStateException) {
            return cancelState
        }
    }

    private suspend fun getChannelChat(botData: BotData, cancelState: CancelState): ChannelChat {
        var channelChat: ChannelChat? = null
        while (channelChat == null) {
            val message = botData.waitTextMessage().first { it.chat.id == context.chatIdOrThrow() }

            checkCancelled(message.content, cancelState)

            val forwardInfo = message.forwardInfo
            if (forwardInfo == null) {
                botData.sendTextMessage(context, "Не похоже, что это пересланное сообщение")
                continue
            }

            channelChat = forwardInfo.fromChannelOrNull()?.chat?.channelChatOrNull()
            if (channelChat == null) {
                botData.sendTextMessage(context, "Не похоже, что сообщение переслано из канала")
            }
        }

        return channelChat
    }

    private suspend fun checkPermissions(
        botData: BotData,
        channelChat: ChannelChat,
        cancelState: CancelState
    ) {
        val rightsMessage = botData.sendTextMessage(
            context,
            "Получилось!\n" +
                "Сейчас добавь бота в администраторы (достаточно прав на отправку сообщений в канал)",
            replyMarkup = inlineKeyboard {
                row {
                    dataButton("✅ Подтвердить", "Confirm")
                }
            }
        )

        while (true) {
            val data = getCallbackQueryOrCancel(botData, cancelState, listOf("Confirm")) ?: continue

            val member = try {
                botData.getChatMember(
                    channelChat,
                    botData.botInfo.component1()
                ).administratorChatMemberOrNull()
            } catch (ex: CommonRequestException) { // api bug
                logger.error(ex)
                null
            }

            if (member == null) {
                botData.sendTextMessage(context, "Бот не является администратором")
            } else {
                if (!member.canPostMessages) {
                    botData.sendTextMessage(context, "Бот не может писать сообщения")
                } else {
                    break
                }
            }
        }

        botData.editMessageReplyMarkup(rightsMessage, replyMarkup = null)
    }

    private fun checkCancelled(content: TextContent, cancelState: CancelState) {
        if (content.text == cancelState.nameWithIcon) {
            throw CancelledStateException(className)
        }
    }

    private suspend fun checkOrConfirm(
        botData: BotData,
        cancelState: CancelState,
        channelChat: ChannelChat
    ) {
        val message = botData.sendTextMessage(
            context,
            "Получилось!\n" +
                "Можешь проверить бота (он отправит сообщение в канал)",
            replyMarkup = inlineKeyboard {
                row {
                    dataButton("\uD83D\uDCCB Проверить бота", "CheckBot")
                }
                row {
                    dataButton("✅ Подтвердить создание канала", "ConfirmAdding")
                }
            }
        )

        while (true) {
            val data = getCallbackQueryOrCancel(botData, cancelState, listOf("CheckBot", "ConfirmAdding")) ?: continue
            when (data) {
                "CheckBot" -> {
                    botData.sendTextMessage(channelChat, "✅ Проверка")
                }
                "ConfirmAdding" -> {
                    botData.editMessageReplyMarkup(message, null)
                    return
                }
                else -> {
                    botData.sendTextMessage(context, "Комманда не найдена")
                }
            }
        }
    }

    private suspend fun getCallbackQueryOrCancel(
        botData: BotData,
        cancelState: CancelState,
        commands: List<String>
    ): String? {
        val tData = merge(
            botData.waitDataCallbackQuery(),
            botData.waitTextMessage()
        ).first()

        if (tData is CommonMessage<*>) {
            val textContent = tData.content.textContentOrNull()
            if (textContent != null) {
                checkCancelled(textContent, cancelState)
            }
        } else if (tData is DataCallbackQuery) {
            val data = tData.data
            if (data in commands) {
                return data
            }
        }

        return null
    }
}
