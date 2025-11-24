package io.github.hhhannahmmmontana.isadamthereyet.states

interface BotStateDecorator : BotState {
    val state: BotState
}
