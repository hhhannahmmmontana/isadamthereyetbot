package io.github.hhhannahmmmontana.isadamthereyet.states.addchannel

import dev.inmo.kslog.common.error
import dev.inmo.kslog.common.logger
import dev.inmo.tgbotapi.bot.exceptions.CommonRequestException
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.chat.members.getChatMember
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.administratorChatMemberOrNull
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.queries.callback.DataCallbackQuery
import io.github.hhhannahmmmontana.isadamthereyet.ManagedBot
import io.github.hhhannahmmmontana.isadamthereyet.states.BotState
import io.github.hhhannahmmmontana.isadamthereyet.states.BotStateWithExtraData
import io.github.hhhannahmmmontana.isadamthereyet.states.IdentifiableBotState
import io.github.hhhannahmmmontana.isadamthereyet.states.ResumableBotState
import io.github.hhhannahmmmontana.isadamthereyet.states.ResumingRequiredState
import io.github.hhhannahmmmontana.isadamthereyet.states.exceptions.BadRequestStateException
import io.github.hhhannahmmmontana.isadamthereyet.states.exceptions.WrongExtraDataException
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.createInlineKeyboardMarkupFromBotStates
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.restoreBotState
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ChannelIdExtraData
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.throwIfNull

class AddChannelGrantPermissionsState(
    override val context: ChatIdentifier
) : ResumableBotState(), IdentifiableBotState {
    override val icon: String = "✅"

    override val viewName: String = "Проверить"

    override val className: String = AddChannelGrantPermissionsState::class.simpleName!!

    override suspend fun invoke(
        bot: ManagedBot,
        extraData: ExtraData?
    ): BotState {
        extraData.throwIfNull()
        bot.sendTextMessage(
            context,
            "Получилось!\n" +
                "Сейчас добавь бота в администраторы (достаточно прав на отправку сообщений в канал)",
            replyMarkup = createInlineKeyboardMarkupFromBotStates(
                listOf(this),
                isCancellable = true
            )
        )

        return BotStateWithExtraData(
            ResumingRequiredState(this),
            extraData!!
        )
    }

    override suspend fun resumeOnDataCallbackQuery(
        bot: ManagedBot,
        data: DataCallbackQuery,
        extraData: ExtraData?
    ): BotState {
        println("I WAS summoned...")
        extraData.throwIfNull()
        if (extraData !is ChannelIdExtraData) {
            throw WrongExtraDataException()
        }

        restoreBotState(
            data.data,
            listOf(this)
        )

        val channelChat = extraData.channelChatId

        val member = try {
            bot.getChatMember(
                channelChat,
                bot.getMe().component1()
            ).administratorChatMemberOrNull()
        } catch (ex: CommonRequestException) { // api bug
            logger.error(ex)
            null
        }

        if (member == null) {
            bot.sendTextMessage(context, "Бот не является администратором")
            throw BadRequestStateException()
        }

        if (!member.canPostMessages) {
            bot.sendTextMessage(context, "Бот не может писать сообщения")
            throw BadRequestStateException()
        }

        return BotStateWithExtraData(
            AddChannelCheckBotState(context),
            extraData
        )
    }
}
