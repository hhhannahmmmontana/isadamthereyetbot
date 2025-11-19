package io.github.hhhannahmmmontana.isadamthereyet.exceptions

abstract class AbsenceOfDataException(
    what: String,
    where: String,
) : IsAdamThereYetException("\"$what\" is not present in \"$where\"")
