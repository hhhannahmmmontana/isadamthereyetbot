package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.micro_utils.fsm.common.State
import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.ManagedBot
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData

sealed interface BotState : State {
    override val context: ChatIdentifier

    suspend fun invoke(
        bot: ManagedBot,
        extraData: ExtraData? = null
    ): BotState?

    val className: String
        get() = this::class.simpleName!!
}
