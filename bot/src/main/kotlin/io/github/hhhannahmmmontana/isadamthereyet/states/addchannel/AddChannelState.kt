package io.github.hhhannahmmmontana.isadamthereyet.states.addchannel

import dev.inmo.tgbotapi.types.ChatIdentifier
import io.github.hhhannahmmmontana.isadamthereyet.ManagedBot
import io.github.hhhannahmmmontana.isadamthereyet.states.BotState
import io.github.hhhannahmmmontana.isadamthereyet.states.IdentifiableBotState
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.createEmptyReplyKeyboard
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData

class AddChannelState(
    override val context: ChatIdentifier
) : IdentifiableBotState {
    override val icon: String = "➕"

    override val viewName: String = "Добавить канал"

    override suspend fun invoke(
        bot: ManagedBot,
        extraData: ExtraData?
    ): BotState {
        bot.sendTextMessage(
            context,
            "Выбрано: создание канала",
            replyMarkup = createEmptyReplyKeyboard(isCancellable = true)
        )
        return AddChannelGetChannelState(context)
    }
}
