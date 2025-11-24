package io.github.hhhannahmmmontana.isadamthereyet.states.addchannel

import dev.inmo.tgbotapi.extensions.utils.channelChatOrNull
import dev.inmo.tgbotapi.extensions.utils.fromChannelOrNull
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import io.github.hhhannahmmmontana.isadamthereyet.ManagedBot
import io.github.hhhannahmmmontana.isadamthereyet.states.BotState
import io.github.hhhannahmmmontana.isadamthereyet.states.BotStateWithExtraData
import io.github.hhhannahmmmontana.isadamthereyet.states.ResumableBotState
import io.github.hhhannahmmmontana.isadamthereyet.states.ResumingRequiredState
import io.github.hhhannahmmmontana.isadamthereyet.states.exceptions.BadRequestStateException
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.createEmptyReplyKeyboard
import io.github.hhhannahmmmontana.isadamthereyet.states.extensions.throwIfCancelled
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ChannelIdExtraData
import io.github.hhhannahmmmontana.isadamthereyet.states.extradata.ExtraData

class AddChannelGetChannelState(
    override val context: ChatIdentifier
) : ResumableBotState() {
    override suspend fun invoke(
        bot: ManagedBot,
        extraData: ExtraData?
    ): BotState? {
        bot.sendTextMessage(
            context,
            "Перешли сообщение из нужного канала",
            replyMarkup = createEmptyReplyKeyboard(isCancellable = true)
        )

        return ResumingRequiredState(this)
    }

    override suspend fun resumeOnMessage(
        bot: ManagedBot,
        message: CommonMessage<*>,
        extraData: ExtraData?
    ): BotState {
        message.throwIfCancelled()
        val forwardInfo = message.forwardInfo
        if (forwardInfo == null) {
            bot.sendTextMessage(context, "Не похоже, что это пересланное сообщение")
            throw BadRequestStateException()
        }

        val channelChat = forwardInfo.fromChannelOrNull()?.chat?.channelChatOrNull()
        if (channelChat == null) {
            bot.sendTextMessage(context, "Не похоже, что сообщение переслано из канала")
            throw BadRequestStateException()
        }

        val extraData = ChannelIdExtraData(channelChat.id)

        return BotStateWithExtraData(
            AddChannelGrantPermissionsState(context),
            extraData
        )
    }
}
