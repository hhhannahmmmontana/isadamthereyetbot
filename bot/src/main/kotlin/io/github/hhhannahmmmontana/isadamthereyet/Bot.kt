package io.github.hhhannahmmmontana.isadamthereyet

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.common.io.Resources
import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.LogLevel
import dev.inmo.kslog.common.defaultMessageFormatterWithErrorPrint
import dev.inmo.kslog.common.setDefaultKSLog
import dev.inmo.tgbotapi.extensions.api.telegramBot
import io.github.hhhannahmmmontana.isadamthereyet.constants.TELEGRAM_TOKEN_ENV_NAME
import io.github.hhhannahmmmontana.isadamthereyet.exceptions.NoTokenException
import io.github.hhhannahmmmontana.isadamthereyet.extensions.buildIasmBehaviour
import io.github.hhhannahmmmontana.isadamthereyet.states.CancelledState
import io.github.hhhannahmmmontana.isadamthereyet.states.ErrorState
import io.github.hhhannahmmmontana.isadamthereyet.states.FinishedState
import io.github.hhhannahmmmontana.isadamthereyet.states.StateService

val TOKEN: String = System.getenv(TELEGRAM_TOKEN_ENV_NAME) ?: throw NoTokenException()

fun loadProps(): BotProperties {
    val serializedProperties = Resources.toString(Resources.getResource("application.yml"), Charsets.UTF_8)
    val mapper = YAMLMapper().registerKotlinModule()
    return mapper.readValue<BotProperties>(serializedProperties)
}
suspend fun main() {
    val bot = telegramBot(TOKEN)
    val properties = loadProps()

    val stateService = StateService(
        properties.clearStatesMinutes,
        listOf(
            ErrorState::class,
            CancelledState::class,
            FinishedState::class
        )
    )

    setDefaultKSLog(
        KSLog { level: LogLevel, tag: String?, message: Any, throwable: Throwable? ->
            if (level != LogLevel.VERBOSE) {
                println(defaultMessageFormatterWithErrorPrint(level, tag, message, throwable))
            }
        },
    )

    bot.buildIasmBehaviour(stateService).join()
}
