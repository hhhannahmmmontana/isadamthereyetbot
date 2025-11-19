package io.github.hhhannahmmmontana.isadamthereyet.extensions

fun String.removeWhitespaces(): String =
    this.filterNot {
        it.isWhitespace()
    }
