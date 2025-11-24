package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.queries.callback.DataCallbackQuery
import io.github.hhhannahmmmontana.isadamthereyet.ManagedBot
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData

class ResumingRequiredState(
    val state: ResumableBotState
) : ResumableBotState() {
    override val context = state.context

    override suspend fun invoke(
        bot: ManagedBot,
        extraData: ExtraData?
    ): BotState? = null

    override suspend fun resumeOnMessage(
        bot: ManagedBot,
        message: CommonMessage<*>,
        extraData: ExtraData?
    ): BotState? = state.resumeOnMessage(bot, message, extraData)


    override suspend fun resumeOnDataCallbackQuery(
        bot: ManagedBot,
        data: DataCallbackQuery,
        extraData: ExtraData?
    ): BotState? = state.resumeOnDataCallbackQuery(bot, data, extraData)
}
