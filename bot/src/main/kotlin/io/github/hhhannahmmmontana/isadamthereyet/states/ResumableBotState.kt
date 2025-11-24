package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.extensions.utils.textContentOrNull
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.queries.callback.DataCallbackQuery
import io.github.hhhannahmmmontana.isadamthereyet.ManagedBot
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.throwIfCancelled
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.throwIfFinished
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData
import io.ktor.server.plugins.BadRequestException

abstract class ResumableBotState : BotState {
    open suspend fun resumeOnMessage(
        bot: ManagedBot,
        message: CommonMessage<*>,
        extraData: ExtraData? = null
    ): BotState? {
        message.throwIfCancelled()
        message.throwIfFinished()
        throw BadRequestException("This state do not require message")
    }

    open suspend fun resumeOnDataCallbackQuery(
        bot: ManagedBot,
        data: DataCallbackQuery,
        extraData: ExtraData? = null
    ): BotState? {
        data.throwIfCancelled()
        data.throwIfFinished()
        throw BadRequestException("This state do not require message")
    }

    protected fun CommonMessage<*>.textOrNull(): String? {
        return this.content.textContentOrNull()?.text
    }
}
