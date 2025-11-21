package io.github.hhhannahmmmontana.isadamthereyet

import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.LogLevel
import dev.inmo.kslog.common.defaultMessageFormatter
import dev.inmo.kslog.common.setDefaultKSLog
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithFSMAndStartLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.MessageContent
import io.github.hhhannahmmmontana.isadamthereyet.constants.TELEGRAM_TOKEN_ENV_NAME
import io.github.hhhannahmmmontana.isadamthereyet.domain.BotData
import io.github.hhhannahmmmontana.isadamthereyet.domain.UserData
import io.github.hhhannahmmmontana.isadamthereyet.exceptions.NoTokenException
import io.github.hhhannahmmmontana.isadamthereyet.states.BotState
import io.github.hhhannahmmmontana.isadamthereyet.states.StartState

val TOKEN: String = System.getenv(TELEGRAM_TOKEN_ENV_NAME) ?: throw NoTokenException()

suspend fun main() {
    val bot = telegramBot(TOKEN)

    setDefaultKSLog(
        KSLog { level: LogLevel, tag: String?, message: Any, throwable: Throwable? ->
            if (level != LogLevel.VERBOSE) {
                println(defaultMessageFormatter(level, tag, message, throwable))
            }
        },
    )

    bot.buildBehaviourWithFSMAndStartLongPolling<BotState> {
        val botData = BotData(this, getMe())

        onCommand("start") { message ->
            startChain(
                StartState(
                    message.chat.id,
                    generateUserData(message)
                )
            )
        }

        onStateOrSubstate<BotState> {
            it.invoke(botData)
        }

    }.join()
}

fun<T : MessageContent> generateUserData(message: CommonMessage<T>): UserData {
    return UserData(message.chat.id.chatId)
}
