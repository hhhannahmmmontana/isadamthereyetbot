package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.domain.BotData
import io.github.hhhannahmmmontana.isadamthereyet.domain.UserData

class CancelState(
    val initStateFun: () -> BotState
) : BotState() {
    val state: BotState by lazy {
        initStateFun()
    }

    override val context: ChatIdentifier = state.context

    override val userData: UserData = state.userData

    override val icon: String = "❌"

    override val viewName: String = "Отмена"

    override val className: String = CancelState::class.simpleName!!

    override suspend fun invoke(botData: BotData): BotState?
        = state.invoke(botData)
}
