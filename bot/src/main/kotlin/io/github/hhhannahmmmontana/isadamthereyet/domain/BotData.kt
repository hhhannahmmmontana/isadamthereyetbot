package io.github.hhhannahmmmontana.isadamthereyet.domain

import dev.inmo.tgbotapi.extensions.api.edit.reply_markup.editMessageReplyMarkup
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitDataCallbackQuery
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitTextMessage
import dev.inmo.tgbotapi.extensions.utils.accessibleMessageOrThrow
import dev.inmo.tgbotapi.extensions.utils.chatIdOrThrow
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import dev.inmo.tgbotapi.types.chat.ExtendedBot
import dev.inmo.tgbotapi.types.message.abstracts.ContentMessage
import io.github.hhhannahmmmontana.isadamthereyet.exceptions.InvariantException
import io.github.hhhannahmmmontana.isadamthereyet.exceptions.NoContextException
import kotlinx.coroutines.flow.first

data class BotData(
    private val context: BehaviourContext,
    val botInfo: ExtendedBot
) : BehaviourContext by context {
    suspend fun pollInlineKeyboardUntilValidData(
        values: Iterable<String>,
        context: ChatIdentifier? = null,
        errorMsg: String? = null,
        originalMessage: ContentMessage<*>? = null,
        closable: Boolean = true
    ): String {
        if (errorMsg != null && context == null) {
            throw NoContextException()
        }
        if (closable && originalMessage == null) {
            throw InvariantException("Original message can't be null if closable is true")
        }

        val data = awaitData(values, context, errorMsg) {
            waitDataCallbackQuery().first().data
        }

        if (closable) {
            editMessageReplyMarkup(
                originalMessage!!.accessibleMessageOrThrow(),
                originalMessage!!.businessConnectionId,
                null
            )
        }

        return data
    }

    suspend fun pollReplyKeyboardUntilValidMessage(
        values: Iterable<String>,
        context: ChatIdentifier? = null,
        errorMsg: String? = null,
        closable: Boolean = true,
        closeMessages: List<String>? = null
    ): String {
        if (errorMsg != null && context == null) {
            throw NoContextException()
        }
        if (closable && (closeMessages == null || context == null)) {
            throw InvariantException("If closable, closeMessage and context should be entered")
        }

        val data = awaitData(values, context, errorMsg) {
            waitTextMessage().first {
                it.chat.id == context!!.chatIdOrThrow()
            }.content.text
        }

        if (closable) {
            sendTextMessage(
                context!!,
                (closeMessages!!)[values.indexOf(data)],
                replyMarkup = ReplyKeyboardRemove()
            )
        }

        return data
    }

    private suspend fun awaitData(
        values: Iterable<String>,
        context: ChatIdentifier?,
        errorMsg: String?,
        getData: suspend () -> String
    ): String {
        var data: String? = null
        while (data == null) {
            val tData = getData()
            if (tData in values) {
                data = tData
            } else if (errorMsg != null) {
                sendTextMessage(context!!, errorMsg)
            }
        }

        return data
    }
}
