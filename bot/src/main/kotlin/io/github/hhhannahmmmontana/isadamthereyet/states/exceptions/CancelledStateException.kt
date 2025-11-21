package io.github.hhhannahmmmontana.isadamthereyet.states.exceptions

class CancelledStateException(state: String) :
    StateException("State \"$state\" is cancelled")