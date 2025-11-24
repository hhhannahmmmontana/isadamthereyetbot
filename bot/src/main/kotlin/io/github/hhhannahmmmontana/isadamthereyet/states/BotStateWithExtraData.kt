package io.github.hhhannahmmmontana.isadamthereyet.states

import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData

class BotStateWithExtraData(
    val state: BotState,
    val extraData: ExtraData
) : BotState by state
