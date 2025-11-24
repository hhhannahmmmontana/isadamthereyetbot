package io.github.hhhannahmmmontana.isadamthereyet

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.buttons.KeyboardMarkup
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.abstracts.ContentMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import io.github.hhhannahmmmontana.isadamthereyet.states.MessageService

class ManagedBot(bot: BehaviourContext) :
    BehaviourContext by bot {
    val messageService = MessageService()

    suspend fun sendTextMessage(
        chatId: ChatIdentifier,
        text: String,
        replyMarkup: KeyboardMarkup? = null
    ): ContentMessage<TextContent> {
        val message = bot.sendTextMessage(chatId, text, replyMarkup = replyMarkup)
        messageService.storeMessage(chatId, message as CommonMessage<*>, this)
        return message
    }
}
