package io.github.hhhannahmmmontana.isadamthereyet.states.extradata

import dev.inmo.tgbotapi.types.ChatIdentifier

data class ChannelIdExtraData(
    val channelChatId: ChatIdentifier
) : ExtraData
