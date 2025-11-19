package io.github.hhhannahmmmontana.isadamthereyet.exceptions

import java.lang.RuntimeException

class NoTokenException : RuntimeException(".env or TELEGRAM_TOKEN value are unavailable");