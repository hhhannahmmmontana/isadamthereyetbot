package io.github.hhhannahmmmontana.isadamthereyet

import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import io.github.hhhannahmmmontana.isadamthereyet.constants.TELEGRAM_TOKEN_ENV_NAME
import io.github.hhhannahmmmontana.isadamthereyet.exceptions.NoTokenException

val TOKEN: String = System.getenv(TELEGRAM_TOKEN_ENV_NAME) ?: throw NoTokenException()

suspend fun main() {
    val bot = telegramBot(TOKEN)

    bot.buildBehaviourWithLongPolling {
        println(getMe())

        onCommand("start") {
            reply(it, "Hi:)")
        }
    }.join()
}
