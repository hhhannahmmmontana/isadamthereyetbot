package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.kslog.common.e
import dev.inmo.kslog.common.logger
import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.states.exceptions.IgnoredStateException
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData
import io.ktor.util.collections.ConcurrentMap
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime.now
import java.util.*
import kotlin.reflect.KClass

class StateService(
    private val removeChatPeriodMinutes: Long,
    private val ignoredStates: List<KClass<out BotState>> = listOf()
) {
    private val q = PriorityQueue<StoredBotState>(
        compareBy { storedBotState ->
            storedBotState.dateTime
        }
    )
    private val chats = ConcurrentMap<ChatIdentifier, StoredBotState>()

    fun storeState(botState: BotState) {
        try {
            val realState = unwrapState(botState)

            val storedBotState = if (realState is BotStateWithExtraData) {
                println("Now I AM, ${realState.state} is stored")
                if (realState.isIgnored()) {
                    throw IgnoredStateException()
                }

                if (!hasChanges(realState)) {
                    return
                }

                StoredBotState(
                    removeChatPeriodMinutes,
                    realState.state,
                    realState.extraData
                )
            } else {
                StoredBotState(
                    removeChatPeriodMinutes,
                    realState,
                    null
                )
            }

            if (chats[realState.context] == null) {
                chats[realState.context] = storedBotState
                q.add(storedBotState)
            } else {
                chats[realState.context] = storedBotState
            }
        } catch (ex: IgnoredStateException) {
            logger.e(ex)
            tryRemoveState(botState.context)
        }
    }

    fun unwrapState(botState: BotState): BotState {
        if (botState.isIgnored()) {
            throw IgnoredStateException()
        }

        return if (botState is BotStateDecorator) {
            botState.state
        } else {
            botState
        }
    }

    fun tryRemoveState(chatId: ChatIdentifier) {
        chats.remove(chatId)
        q.removeIf { it.state.context == chatId }
    }

    fun tryRestoreState(chatId: ChatIdentifier): Pair<BotState, ExtraData?>? {
        val ans = chats[chatId]
        val botState = ans?.state
        if (botState != null) {
            return Pair(botState, ans.extraData)
        }

        return null
    }

    fun replaceOrRemoveState(
        context: ChatIdentifier,
        botState: BotState?
    ) {
        tryRemoveState(context)
        if (botState != null) {
            storeState(botState)
        }
    }

    fun replaceOrKeep(
        botState: BotState?
    ) {
        if (botState != null) {
            storeState(botState)
        }
    }

    fun clearStates() {
        val now = now().toKotlinLocalDateTime()
        while (!q.isEmpty() && q.first().dateTime <= now) {
            val state = q.poll().state
            chats.remove(state.context)
        }
    }

    private fun hasChanges(botState: BotState): Boolean {
        return botState != chats[botState.context]?.state
    }

    private fun hasChanges(botState: BotStateWithExtraData): Boolean {
        return botState.state != chats[botState.context]?.state ||
            botState.extraData != chats[botState.context]?.extraData
    }

    private fun BotState.isIgnored(): Boolean {
        return this::class in ignoredStates
    }
}
