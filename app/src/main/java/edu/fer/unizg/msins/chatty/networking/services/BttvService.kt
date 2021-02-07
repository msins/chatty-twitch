package edu.fer.unizg.msins.chatty.networking.services

import edu.fer.unizg.msins.chatty.emotes.services.bttv.BttvChannelInfo
import edu.fer.unizg.msins.chatty.emotes.services.bttv.BttvEmoteDescriptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BttvService {
    @GET("emotes/global")
    suspend fun getGlobalEmotes(): Response<List<BttvEmoteDescriptor>>

    @GET("users/twitch/{channelId}")
    suspend fun getChannelEmotes(@Path("channelId") channelId: String): Response<BttvChannelInfo>
}