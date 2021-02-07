package edu.fer.unizg.msins.chatty.emotes.services.bttv

data class BttvChannelInfo(
    val id: String,
    val sharedEmotes: List<BttvEmoteDescriptor>,
    val channelEmotes: List<BttvEmoteDescriptor>
)