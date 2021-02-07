package edu.fer.unizg.msins.chatty.networking.services

import edu.fer.unizg.msins.chatty.networking.dto.TwitchEmotesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TwitchEmotesService {
    @GET("channels/{channelId}")
    suspend fun getChannelEmotes(@Path("channelId") channelId: String): Response<TwitchEmotesDto>
}