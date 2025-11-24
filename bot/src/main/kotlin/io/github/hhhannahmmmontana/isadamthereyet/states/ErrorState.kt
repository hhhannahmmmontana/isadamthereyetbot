package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.ManagedBot
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData

class ErrorState(
    override val context: ChatIdentifier
) : BotState {
    override suspend fun invoke(
        bot: ManagedBot,
        extraData: ExtraData?
    ): BotState {
        bot.sendTextMessage(
            context,
            "Произошла ошибка\n" +
                "Если она повторится, напиши @hhhannahmmmontana"
        )

        return StartState(context)
    }
}
