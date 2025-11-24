package io.github.hhhannahmmmontana.isadamthereyet.states.addchannel

import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.queries.callback.DataCallbackQuery
import io.github.hhhannahmmmontana.isadamthereyet.ManagedBot
import io.github.hhhannahmmmontana.isadamthereyet.states.BotState
import io.github.hhhannahmmmontana.isadamthereyet.states.BotStateWithExtraData
import io.github.hhhannahmmmontana.isadamthereyet.states.IdentifiableBotState
import io.github.hhhannahmmmontana.isadamthereyet.states.ResumableBotState
import io.github.hhhannahmmmontana.isadamthereyet.states.ResumingRequiredState
import io.github.hhhannahmmmontana.isadamthereyet.states.exceptions.WrongExtraDataException
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.createInlineKeyboardMarkupFromBotStates
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.createStates
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.restoreBotState
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ChannelIdExtraData
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.throwIfNull

class AddChannelCheckBotState(
    override val context: ChatIdentifier
) : ResumableBotState(), IdentifiableBotState {
    override val icon: String = "\uD83D\uDD0E"

    override val viewName: String = "Проверка"

    val linkedStates by lazy {
        createStates(context, listOf(this::class))
    }

    override suspend fun invoke(
        bot: ManagedBot,
        extraData: ExtraData?
    ): BotState {
        extraData.throwIfNull()
        bot.sendTextMessage(
            context,
            "Получилось!\n" +
                "Можешь проверить бота (он отправит сообщение в канал)",
            replyMarkup = createInlineKeyboardMarkupFromBotStates(
                linkedStates,
                isCancellable = true,
                isFinishable = true
            )
        )

        return BotStateWithExtraData(
            ResumingRequiredState(this),
            extraData!!
        )
    }

    override suspend fun resumeOnDataCallbackQuery(
        bot: ManagedBot,
        data: DataCallbackQuery,
        extraData: ExtraData?
    ): BotState? {
        extraData.throwIfNull()
        if (extraData !is ChannelIdExtraData) {
            throw WrongExtraDataException()
        }

        restoreBotState(data.data, linkedStates)
        bot.sendTextMessage(extraData.channelChatId, "✅ Проверка")

        return ResumingRequiredState(this)
    }
}
