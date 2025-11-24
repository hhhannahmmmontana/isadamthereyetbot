package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.queries.callback.DataCallbackQuery
import io.github.hhhannahmmmontana.isadamthereyet.ManagedBot
import io.github.hhhannahmmmontana.isadamthereyet.states.ResumingRequiredState
import io.github.hhhannahmmmontana.isadamthereyet.states.addchannel.AddChannelState
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.createInlineKeyboardMarkupFromBotStates
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.createStates
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.restoreBotState
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.throwIfCancelled
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData

class ViewChannelsState(
    override val context: ChatIdentifier
) : ResumableBotState(), IdentifiableBotState {
    override val icon: String = "⚙\uFE0F"

    override val viewName: String = "Управление каналами"

    val linkedBotStates: List<IdentifiableBotState> by lazy {
        createStates(
            context,
            listOf(
                AddChannelState::class
            )
        )
    }

    override suspend fun invoke(
        bot: ManagedBot,
        extraData: ExtraData?
    ): BotState? {
        bot.sendTextMessage(
            context,
            "Выберите действие",
            replyMarkup = createInlineKeyboardMarkupFromBotStates(
                linkedBotStates,
                isCancellable = true
            )
        )

        return ResumingRequiredState(this)
    }

    override suspend fun resumeOnDataCallbackQuery(
        bot: ManagedBot,
        data: DataCallbackQuery,
        extraData: ExtraData?
    ): BotState? {
        data.throwIfCancelled()
        val action = data.data
        return restoreBotState(action, linkedBotStates)
    }
}
