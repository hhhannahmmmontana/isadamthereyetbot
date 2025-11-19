package io.github.hhhannahmmmontana.isadamthereyet.parcers

import io.github.hhhannahmmmontana.isadamthereyet.extensions.removeWhitespaces
import io.github.hhhannahmmmontana.isadamthereyet.parcers.exceptions.ParserException

class ListParser {
    private constructor()

    companion object {
        val instance by lazy {
            ListParser()
        }
    }

    fun stringToList(rawList: String): List<String> {
        if (rawList.first() != '[' || rawList.last() != ']') {
            throw ParserException(rawList)
        }
        if (rawList.length == 2) {
            return emptyList()
        }
        return rawList
            .substring(1, rawList.length - 1)
            .removeWhitespaces()
            .split(',')
    }
}
