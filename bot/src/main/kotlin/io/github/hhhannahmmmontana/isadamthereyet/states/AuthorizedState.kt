package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.domain.BotData
import io.github.hhhannahmmmontana.isadamthereyet.domain.UserData

class AuthorizedState(
    override val context: ChatIdentifier,
    override val userData: UserData
) : BotState {
    override suspend fun invoke(botData: BotData): BotState {
        botData.sendTextMessage(
            context,
            "Привет! Я ${botData.botInfo.firstName}, " +
                "Ты администратор. " +
                "Ты пока ничего не можешь делать."
        )

        return NoneState(context, userData)
    }
}
