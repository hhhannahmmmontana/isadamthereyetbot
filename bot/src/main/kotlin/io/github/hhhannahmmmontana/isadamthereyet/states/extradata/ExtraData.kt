package io.github.hhhannahmmmontana.isadamthereyet.states.extradata

import io.github.hhhannahmmmontana.isadamthereyet.states.exceptions.MissingExtraDataException

interface ExtraData

fun ExtraData?.throwIfNull() {
    if (this == null) {
        throw MissingExtraDataException()
    }
}
