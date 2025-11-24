package io.github.hhhannahmmmontana.isadamthereyet.states

interface IdentifiableBotState : BotState {
    val icon: String

    val viewName: String

    val nameWithIcon: String
        get() = "$icon $viewName"
}
