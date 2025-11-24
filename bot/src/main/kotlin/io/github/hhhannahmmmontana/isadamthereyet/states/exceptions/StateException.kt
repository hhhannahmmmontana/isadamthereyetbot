package io.github.hhhannahmmmontana.isadamthereyet.states.exceptions

import io.github.hhhannahmmmontana.isadamthereyet.exceptions.IsAdamThereYetException

sealed class StateException(message: String) :
    IsAdamThereYetException(message)
