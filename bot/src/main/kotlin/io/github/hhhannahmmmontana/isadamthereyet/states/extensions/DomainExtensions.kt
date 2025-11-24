package io.github.hhhannahmmmontana.isadamthereyet.states.extensions

import dev.inmo.tgbotapi.extensions.utils.fromUserOrNull
import dev.inmo.tgbotapi.types.ChatIdentifier

fun ChatIdentifier.isFromUser(): Boolean {
    return this.fromUserOrNull() != null
}
