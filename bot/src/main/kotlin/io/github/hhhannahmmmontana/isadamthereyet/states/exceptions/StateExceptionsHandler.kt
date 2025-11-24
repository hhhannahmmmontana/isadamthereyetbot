package io.github.hhhannahmmmontana.isadamthereyet.states.exceptions

import dev.inmo.kslog.common.d
import dev.inmo.kslog.common.e
import dev.inmo.kslog.common.logger
import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.states.BotState
import io.github.hhhannahmmmontana.isadamthereyet.states.CancelledState
import io.github.hhhannahmmmontana.isadamthereyet.states.ErrorState
import io.github.hhhannahmmmontana.isadamthereyet.states.FinishedState

object StateExceptionsHandler {
    fun handleStateException(
        ex: StateException,
        context: ChatIdentifier
    ): BotState? {
        when (ex) {
            is CancelledStateException,
            is FinishedStateException,
            is StateNotFoundException -> {
                logger.d(ex)
            }
            else -> {
                logger.e(ex)
            }
        }
        return when (ex) {
            is BadRequestStateException -> null
            is CancelledStateException -> CancelledState(context)
            is FinishedStateException -> FinishedState(context)
            is MissingExtraDataException -> ErrorState(context)
            is StateNotFoundException -> null
            is UnknownActionException -> null
            is WeirdConstructorException -> ErrorState(context)
            is WrongExtraDataException -> ErrorState(context)
            is IgnoredStateException -> null
        }
    }
}

