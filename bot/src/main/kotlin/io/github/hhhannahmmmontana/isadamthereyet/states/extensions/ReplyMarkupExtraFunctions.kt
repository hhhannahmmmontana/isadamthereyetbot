package io.github.hhhannahmmmontana.isadamthereyet.states.extensions

import dev.inmo.tgbotapi.extensions.utils.types.buttons.dataButton
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardMarkup
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup
import dev.inmo.tgbotapi.utils.row
import io.github.hhhannahmmmontana.isadamthereyet.states.BotState
import io.github.hhhannahmmmontana.isadamthereyet.states.IdentifiableBotState
import io.github.hhhannahmmmontana.isadamthereyet.states.exceptions.StateNotFoundException
import io.github.hhhannahmmmontana.isadamthereyet.states.exceptions.WeirdConstructorException
import kotlin.reflect.KClass

fun createStates(
    context: ChatIdentifier,
    botStates: List<KClass<out IdentifiableBotState>>
): List<IdentifiableBotState> {
    val ans = mutableListOf<IdentifiableBotState>()
    for (state in botStates) {
        val suitableConstructor =
            state.constructors.firstOrNull { constructor ->
                constructor.parameters.size == 1 &&
                    constructor.parameters[0].type.classifier == ChatIdentifier::class
            }

        if (suitableConstructor == null) {
            throw WeirdConstructorException()
        }

        ans.add(suitableConstructor.call(context))
    }

    return ans.toList()
}

fun createInlineKeyboardMarkupFromBotStates(
    botStates: List<IdentifiableBotState>,
    rowSize: Int = 1,
    isCancellable: Boolean = false,
    isFinishable: Boolean = false
): InlineKeyboardMarkup {
    require(rowSize >= 1) {
        "Row size can not be less than 1"
    }
    val names = botStates.map { state ->
        Pair(state.nameWithIcon, state.className)
    }

    val iter = names.iterator()

    return inlineKeyboard {
        while (iter.hasNext()) {
            row {
                repeat(rowSize) {
                    if (!iter.hasNext()) {
                        return@row
                    }
                    val (key, value) = iter.next()
                    dataButton(key, value)
                }
            }
            if (isFinishable) {
                row {
                    dataButton(
                        FINISHED_STATE_NAME_WITH_ICON,
                        FINISHED_STATE_DATA_CALLBACK_QUERY_NAME
                    )
                }
            }
            if (isCancellable) {
                row {
                    dataButton(
                        CANCELLED_STATE_NAME_WITH_ICON,
                        CANCELLED_STATE_DATA_CALLBACK_QUERY_NAME
                    )
                }
            }
        }
    }
}

fun createReplyKeyboardMarkupFromBotStates(
    botStates: List<IdentifiableBotState>,
    rowSize: Int = 1,
    isCancellable: Boolean = false,
    isFinishable: Boolean = false,
    isOneTime: Boolean = false
): ReplyKeyboardMarkup {
    require(rowSize >= 1) {
        "Row size can not be less than 1"
    }
    val names = botStates.map { state ->
        state.nameWithIcon
    }

    val iter = names.iterator()

    return replyKeyboard(oneTimeKeyboard = isOneTime) {
        while (iter.hasNext()) {
            row {
                repeat(rowSize) {
                    if (!iter.hasNext()) {
                        return@row
                    }
                    val key = iter.next()
                    simpleButton(key)
                }
            }
            if (isFinishable) {
                row {
                    simpleButton(FINISHED_STATE_NAME_WITH_ICON)
                }
            }
            if (isCancellable) {
                row {
                    simpleButton(CANCELLED_STATE_NAME_WITH_ICON)
                }
            }
        }
    }
}

fun createEmptyReplyKeyboard(
    isCancellable: Boolean = false,
    isFinishable: Boolean = false
): ReplyKeyboardMarkup {
    return replyKeyboard {
        if (isFinishable) {
            row {
                simpleButton(FINISHED_STATE_NAME_WITH_ICON)
            }
        }
        if (isCancellable) {
            row {
                simpleButton(CANCELLED_STATE_NAME_WITH_ICON)
            }
        }
    }
}

fun createEmptyInlineKeyboard(
    isCancellable: Boolean = false,
    isFinishable: Boolean = false
): InlineKeyboardMarkup {
    return inlineKeyboard {
        if (isFinishable) {
            row {
                dataButton(
                    FINISHED_STATE_NAME_WITH_ICON,
                    FINISHED_STATE_DATA_CALLBACK_QUERY_NAME
                )
            }
        }
        if (isCancellable) {
            row {
                dataButton(
                    CANCELLED_STATE_NAME_WITH_ICON,
                    CANCELLED_STATE_DATA_CALLBACK_QUERY_NAME
                )
            }
        }
    }
}

fun restoreBotState(
    stateName: String,
    botStates: List<IdentifiableBotState>
): BotState {
    stateName.throwIfCancelled()
    stateName.throwIfFinished()

    val ans = botStates.firstOrNull { state ->
        stateName in listOf(state.className, state.viewName, state.nameWithIcon)
    }

    return ans ?: throw StateNotFoundException()
}
