package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.domain.BotData
import io.github.hhhannahmmmontana.isadamthereyet.domain.UserData
import kotlinx.coroutines.flow.first

class NoneState(
    override val context: ChatIdentifier,
    override val userData: UserData
) : BotState {
    override suspend fun invoke(botData: BotData): BotState {
        botData.waitAnyContent().first()

        return NoneState(context, userData)
    }
}
