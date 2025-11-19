package io.github.hhhannahmmmontana.isadamthereyet

import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.LogLevel
import dev.inmo.kslog.common.defaultMessageFormatter
import dev.inmo.kslog.common.setDefaultKSLog
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithFSMAndStartLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.onStateOrSubstate
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.command
import dev.inmo.tgbotapi.extensions.utils.asFromUser
import dev.inmo.tgbotapi.extensions.utils.ifWithUser
import dev.inmo.tgbotapi.extensions.utils.usernameChatOrNull
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.MessageContent
import io.github.hhhannahmmmontana.isadamthereyet.constants.ADMIN_LIST_ENV_NAME
import io.github.hhhannahmmmontana.isadamthereyet.constants.TELEGRAM_TOKEN_ENV_NAME
import io.github.hhhannahmmmontana.isadamthereyet.domain.BotData
import io.github.hhhannahmmmontana.isadamthereyet.domain.UserData
import io.github.hhhannahmmmontana.isadamthereyet.exceptions.NoTokenException
import io.github.hhhannahmmmontana.isadamthereyet.parcers.TelegramUsernameDogGroomer
import io.github.hhhannahmmmontana.isadamthereyet.states.AuthorizedState
import io.github.hhhannahmmmontana.isadamthereyet.states.BotState
import io.github.hhhannahmmmontana.isadamthereyet.states.NoneState
import io.github.hhhannahmmmontana.isadamthereyet.states.StartState
import io.github.hhhannahmmmontana.isadamthereyet.states.UnauthorizedState

val TOKEN: String = System.getenv(TELEGRAM_TOKEN_ENV_NAME) ?: throw NoTokenException()

suspend fun main() {
    val bot = telegramBot(TOKEN)
    val adminsString = System.getenv(ADMIN_LIST_ENV_NAME) ?: "[]"

    setDefaultKSLog(
        KSLog { level: LogLevel, tag: String?, message: Any, throwable: Throwable? ->
            if (level != LogLevel.VERBOSE) {
                println(defaultMessageFormatter(level, tag, message, throwable))
            }
        },
    )

    bot.buildBehaviourWithFSMAndStartLongPolling<BotState> {
        val botData = BotData(bot, this, getMe(), adminsString)

        onStateOrSubstate<BotState> {
            it.invoke(botData)
        }

        command("start") { message ->
            startChain(
                StartState(
                    message.chat.id,
                    generateUserData(message)
                )
            )
        }
    }.join()
}

fun<T : MessageContent> generateUserData(message: CommonMessage<T>): UserData {
    var username = message.chat.usernameChatOrNull()?.username?.username
    if (username != null) {
        username = TelegramUsernameDogGroomer.instance.groomUsername(username)
    }

    return UserData(username)
}
