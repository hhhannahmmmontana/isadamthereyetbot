package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitAnyContent
import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.domain.BotData
import io.github.hhhannahmmmontana.isadamthereyet.domain.UserData
import kotlinx.coroutines.flow.first

class NoneState(
    override val context: ChatIdentifier,
    override val userData: UserData
) : BotState() {
    override val icon: String = "\uD83E\uDD37\u200D♀\uFE0F"

    override val viewName: String = "Не делать ничего"

    override val className: String = NoneState::class.simpleName!!

    override suspend fun invoke(botData: BotData): BotState {
        botData.waitAnyContent().first()

        return NoneState(context, userData)
    }
}
