package io.github.hhhannahmmmontana.isadamthereyet.states.extensions

import dev.inmo.tgbotapi.extensions.utils.textContentOrNull
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.queries.callback.DataCallbackQuery
import io.github.hhhannahmmmontana.isadamthereyet.states.exceptions.CancelledStateException
import io.github.hhhannahmmmontana.isadamthereyet.states.exceptions.FinishedStateException

const val CANCELLED_STATE_VIEW_NAME = "Отмена"
const val CANCELLED_STATE_ICON = "❌"
const val CANCELLED_STATE_NAME_WITH_ICON = "$CANCELLED_STATE_ICON $CANCELLED_STATE_VIEW_NAME"
const val CANCELLED_STATE_DATA_CALLBACK_QUERY_NAME = "Cancel"

fun String.throwIfCancelled() {
    if (
        this in listOf(
            CANCELLED_STATE_VIEW_NAME,
            CANCELLED_STATE_ICON,
            CANCELLED_STATE_NAME_WITH_ICON,
            CANCELLED_STATE_DATA_CALLBACK_QUERY_NAME
        )
    ) {
        throw CancelledStateException()
    }
}

fun CommonMessage<*>.throwIfCancelled() {
    content.textContentOrNull()?.text?.throwIfCancelled()
}

fun DataCallbackQuery.throwIfCancelled() {
    data.throwIfCancelled()
}

const val FINISHED_STATE_VIEW_NAME = "Завершить"
const val FINISHED_STATE_ICON = "✅"
const val FINISHED_STATE_NAME_WITH_ICON = "$FINISHED_STATE_ICON $FINISHED_STATE_VIEW_NAME"
const val FINISHED_STATE_DATA_CALLBACK_QUERY_NAME = "Finished"

fun String.throwIfFinished() {
    if (
        this in listOf(
            FINISHED_STATE_VIEW_NAME,
            FINISHED_STATE_ICON,
            FINISHED_STATE_NAME_WITH_ICON,
            FINISHED_STATE_DATA_CALLBACK_QUERY_NAME
        )
    ) {
        throw FinishedStateException()
    }
}

fun CommonMessage<*>.throwIfFinished() {
    content.textContentOrNull()?.text?.throwIfFinished()
}

fun DataCallbackQuery.throwIfFinished() {
    data.throwIfCancelled()
}
