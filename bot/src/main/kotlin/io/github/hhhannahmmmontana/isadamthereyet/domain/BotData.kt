package io.github.hhhannahmmmontana.isadamthereyet.domain

import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitAnyContent
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.chat.ExtendedBot
import io.github.hhhannahmmmontana.isadamthereyet.parcers.ListParser

data class BotData(
    val bot: RequestsExecutor,
    val context: BehaviourContext,
    val botInfo: ExtendedBot,
    val admins: List<String>
) {
    constructor(
        bot: RequestsExecutor,
        behaviourContext: BehaviourContext,
        botInfo: ExtendedBot,
        adminsString: String
    ) : this(
        bot,
        behaviourContext,
        botInfo,
        ListParser.instance.stringToList(adminsString),
    )

    suspend fun sendTextMessage(
        chatId: ChatIdentifier,
        message: String
    ) = bot.sendTextMessage(chatId, message)

    fun waitAnyContent() = context.waitAnyContent()
}
