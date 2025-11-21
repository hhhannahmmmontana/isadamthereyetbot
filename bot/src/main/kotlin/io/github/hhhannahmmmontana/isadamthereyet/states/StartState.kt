package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.utils.row
import io.github.hhhannahmmmontana.isadamthereyet.domain.BotData
import io.github.hhhannahmmmontana.isadamthereyet.domain.UserData

class StartState(
    override val context: ChatIdentifier,
    override val userData: UserData
) : BotState() {
    override val icon: String = "\uD83D\uDE80"

    override val viewName: String = "Начать работу"

    override val className: String = StartState::class.simpleName!!

    override suspend fun invoke(botData: BotData): BotState {
        val states = createStates()

        botData.sendTextMessage(
            context,
            "Привет! Я Ждун.\n" +
                "Я веду телеграм каналы, где люди что-то или кого-то ждут.\n" +
                "Выбери комманду, я помогу все настроить",
            replyMarkup = replyKeyboard {
                for (command in states) {
                    row {
                        simpleButton(command.nameWithIcon)
                    }
                }
            }
        )

        val data = botData.pollReplyKeyboardUntilValidMessage(
            states.map { it.nameWithIcon },
            context,
            "Нету такой комманды",
            true,
            states.map { "Выбран вариант: ${it.viewName}" }
        )

        return states.first { it.nameWithIcon == data }
    }

    fun createStates(): List<BotState> {
        return listOf(
            ChannelSelectState(context, userData)
        )
    }
}
