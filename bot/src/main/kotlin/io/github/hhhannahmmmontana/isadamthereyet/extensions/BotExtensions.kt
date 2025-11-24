package io.github.hhhannahmmmontana.isadamthereyet.extensions

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.deleteMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithFSMAndStartLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onDataCallbackQuery
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import dev.inmo.tgbotapi.extensions.utils.fromUserMessageOrNull
import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.ManagedBot
import io.github.hhhannahmmmontana.isadamthereyet.states.BotState
import io.github.hhhannahmmmontana.isadamthereyet.states.ResumableBotState
import io.github.hhhannahmmmontana.isadamthereyet.states.StartState
import io.github.hhhannahmmmontana.isadamthereyet.states.StateService
import io.github.hhhannahmmmontana.isadamthereyet.states.exceptions.StateException
import io.github.hhhannahmmmontana.isadamthereyet.states.exceptions.StateExceptionsHandler.handleStateException
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData
import kotlinx.coroutines.Job

suspend fun TelegramBot.buildIasmBehaviour(stateService: StateService): Job {
    return buildBehaviourWithFSMAndStartLongPolling<BotState> {
        val bot = ManagedBot(this)

        onCommand("start") { message ->
            if (message.fromUserMessageOrNull() != null) {
                stateService.clearStates()
                stateService.tryRemoveState(message.chat.id)
                startChain(StartState(message.chat.id))
            }
        }

        onText { message ->
            deleteMessage(message)
            if (message.fromUserMessageOrNull() != null) {
                val newState = restore(
                    stateService,
                    message.chat.id
                ) { resumableBotState, extraData ->
                    resumableBotState.resumeOnMessage(bot, message, extraData)
                }

                stateService.replaceOrKeep(newState)
                if (newState != null) {
                    startChain(newState)
                }
            }
        }

        onDataCallbackQuery { data ->
            val newState = restore(
                stateService,
                data.from.id
            ) { resumableBotState, extraData ->
                resumableBotState.resumeOnDataCallbackQuery(bot, data, extraData)
            }

            if (newState != null) {
                startChain(newState)
            }
        }

        onStateOrSubstate<BotState> { state ->
            stateService.clearStates()
            var restoredState = stateService.tryRestoreState(state.context)
            if (restoredState == null) {
                stateService.storeState(state)
                restoredState = Pair(state, null)
            }

            try {
                val newState = state.invoke(bot, restoredState.second)
                stateService.replaceOrKeep(newState)
                newState
            } catch (ex: StateException) {
                handleStateException(ex, state.context)
            }
        }
    }
}

suspend fun restore(
    stateService: StateService,
    chatId: ChatIdentifier,
    getState: suspend (ResumableBotState, ExtraData?) -> BotState?
): BotState? {
    val restoredState = stateService.tryRestoreState(chatId)
    stateService.clearStates()

    return try {
        when (val state = restoredState?.first) {
            is ResumableBotState -> getState(state, restoredState.second)
            else -> null
        }
    } catch (ex: StateException) {
        handleStateException(ex, chatId)
    }
}
