package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.domain.BotData
import io.github.hhhannahmmmontana.isadamthereyet.domain.UserData

class StartState(
    override val context: ChatIdentifier,
    override val userData: UserData
) : BotState {
    override suspend fun invoke(botData: BotData): BotState {
        if (userData.username in botData.admins) {
            return AuthorizedState(context, userData)
        }

        return UnauthorizedState(context, userData)
    }
}
