package io.github.hhhannahmmmontana.isadamthereyet.states

import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime.now

class StoredBotState(
    val dateTime: LocalDateTime,
    val state: BotState,
    val extraData: ExtraData?
) {
    constructor(
        removeChatPeriodMinutes: Long,
        botState: BotState,
        extraData: ExtraData?
    ) : this(
        now()
            .plusMinutes(removeChatPeriodMinutes)
            .toKotlinLocalDateTime(),
        botState,
        extraData
    )
}
