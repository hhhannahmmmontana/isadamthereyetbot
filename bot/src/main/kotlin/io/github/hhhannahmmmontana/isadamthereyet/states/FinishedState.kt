package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.ManagedBot
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.FINISHED_STATE_DATA_CALLBACK_QUERY_NAME
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.FINISHED_STATE_ICON
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.FINISHED_STATE_NAME_WITH_ICON
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.FINISHED_STATE_VIEW_NAME
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData

class FinishedState(
    override val context: ChatIdentifier
) : IdentifiableBotState {
    override val icon: String = FINISHED_STATE_ICON

    override val viewName: String = FINISHED_STATE_VIEW_NAME

    override val nameWithIcon: String
        get() = FINISHED_STATE_NAME_WITH_ICON

    override val className: String
        get() = FINISHED_STATE_DATA_CALLBACK_QUERY_NAME

    override suspend fun invoke(
        bot: ManagedBot,
        extraData: ExtraData?
    ): BotState {
        return StartState(context)
    }
}
