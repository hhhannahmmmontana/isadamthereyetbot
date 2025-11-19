package io.github.hhhannahmmmontana.isadamthereyet.parcers.exceptions

import io.github.hhhannahmmmontana.isadamthereyet.exceptions.IsAdamThereYetException

class ParserException(data: String) :
    IsAdamThereYetException("Data \"${data}\" is not parsable")
