package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.micro_utils.fsm.common.State
import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.domain.BotData
import io.github.hhhannahmmmontana.isadamthereyet.domain.UserData

sealed class BotState : State {
    abstract override val context: ChatIdentifier

    abstract val userData: UserData

    abstract val icon: String

    abstract val viewName: String

    val nameWithIcon: String
        get() = "$icon $viewName"

    abstract val className: String

    abstract suspend fun invoke(botData: BotData): BotState?
}
