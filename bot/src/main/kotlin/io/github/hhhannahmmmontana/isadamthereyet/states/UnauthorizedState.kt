package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.domain.BotData
import io.github.hhhannahmmmontana.isadamthereyet.domain.UserData

class UnauthorizedState(
    override val context: ChatIdentifier,
    override val userData: UserData
) : BotState {
    override suspend fun invoke(botData: BotData): BotState {
        botData.sendTextMessage(
            context,
            "Привет! Я ${botData.botInfo.firstName}. " +
                "Ты не являешься администратором. " +
                "Ты ничего не можешь делать."
        )

        return NoneState(context, userData)
    }
}
