package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.dataButton
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.utils.row
import io.github.hhhannahmmmontana.isadamthereyet.domain.BotData
import io.github.hhhannahmmmontana.isadamthereyet.domain.UserData

class ChannelSelectState(
    override val context: ChatIdentifier,
    override val userData: UserData
) : BotState() {
    override val icon: String = "\uD83D\uDCCB"

    override val viewName: String = "Просмотр каналов"

    override val className: String = ChannelSelectState::class.simpleName!!

    override suspend fun invoke(botData: BotData): BotState {
        val states = createStates()

        val keyboard = inlineKeyboard {
            for (state in states) {
                row {
                    dataButton(state.nameWithIcon, state.className)
                }
            }
        }

        val message = botData.sendTextMessage(context, "Выбери действие", replyMarkup = keyboard)

        val data = botData.pollInlineKeyboardUntilValidData(
            states.map { it.className },
            context,
            "Нет такой комманды",
            originalMessage = message,
            closable = true
        )

        return states.first { it.className == data }
    }

    fun createStates(): List<BotState> {
        return listOf(
            AddChannelState(context, userData),
            CancelState { StartState(context, userData) }
        )
    }
}