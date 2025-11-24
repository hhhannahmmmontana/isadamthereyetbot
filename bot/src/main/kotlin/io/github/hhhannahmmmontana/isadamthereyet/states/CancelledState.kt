package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.ManagedBot
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.CANCELLED_STATE_DATA_CALLBACK_QUERY_NAME
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.CANCELLED_STATE_ICON
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.CANCELLED_STATE_NAME_WITH_ICON
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.CANCELLED_STATE_VIEW_NAME
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData

class CancelledState(
    override val context: ChatIdentifier,
) : IdentifiableBotState {
    override val icon: String = CANCELLED_STATE_ICON

    override val viewName: String = CANCELLED_STATE_VIEW_NAME

    override val nameWithIcon: String
        get() = CANCELLED_STATE_NAME_WITH_ICON

    override val className: String
        get() = CANCELLED_STATE_DATA_CALLBACK_QUERY_NAME

    override suspend fun invoke(
        bot: ManagedBot,
        extraData: ExtraData?
    ): BotState {
        return StartState(context)
    }
}
