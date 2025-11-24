package io.github.hhhannahmmmontana.isadamthereyet.states

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.oneTimeKeyboardField
import io.github.hhhannahmmmontana.isadamthereyet.ManagedBot
import io.github.hhhannahmmmontana.isadamthereyet.states.exceptions.BadRequestStateException
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.createReplyKeyboardMarkupFromBotStates
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.createStates
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.restoreBotState
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData

class StartState(
    override val context: ChatIdentifier
) : ResumableBotState() {
    private val linkedStates: List<IdentifiableBotState> by lazy {
        createStates(
            context,
            listOf(
                ViewChannelsState::class
            )
        )
    }

    override suspend fun invoke(
        bot: ManagedBot,
        extraData: ExtraData?
    ): BotState? {
        bot.sendTextMessage(
            context,
            "Привет! Я Ждун.\n" +
                "Я веду телеграм каналы, где люди что-то или кого-то ждут.\n" +
                "Выбери комманду, я помогу все настроить",
            replyMarkup = createReplyKeyboardMarkupFromBotStates(linkedStates),
        )

        return null
    }

    override suspend fun resumeOnMessage(
        bot: ManagedBot,
        message: CommonMessage<*>,
        extraData: ExtraData?
    ): BotState {
        val text = message.textOrNull() ?: throw BadRequestStateException()
        return restoreBotState(text, linkedStates)
    }
}
