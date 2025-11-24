package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.extensions.api.deleteMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import io.ktor.util.collections.ConcurrentMap
import kotlin.collections.set

class MessageService {
    private val messages = ConcurrentMap<ChatIdentifier, CommonMessage<*>>()

    suspend fun storeMessage(
        context: ChatIdentifier,
        message: CommonMessage<*>,
        bot: BehaviourContext
    ) {
        val oldMessage = messages[context]
        if (oldMessage != null) {
            bot.deleteMessage(oldMessage)
        }

        messages[context] = message
    }
}
