package io.github.hhhannahmmmontana.isadamthereyet.parcers

import dev.inmo.kslog.common.debug
import dev.inmo.kslog.common.logger

class TelegramUsernameDogGroomer {
    private constructor()

    companion object {
        val instance by lazy {
            TelegramUsernameDogGroomer()
        }
    }

    fun groomUsername(username: String): String {
        logger.debug("Joke", "You are really mature for your age...")

        return username.substring(1)
    }
}
