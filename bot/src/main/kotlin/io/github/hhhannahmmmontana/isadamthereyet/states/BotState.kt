package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.micro_utils.fsm.common.State
import io.github.hhhannahmmmontana.isadamthereyet.domain.BotData
import io.github.hhhannahmmmontana.isadamthereyet.domain.UserData

sealed interface BotState : State {
    val userData: UserData

    suspend fun invoke(botData: BotData): BotState
}
