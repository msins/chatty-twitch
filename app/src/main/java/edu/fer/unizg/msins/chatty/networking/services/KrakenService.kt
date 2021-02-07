package edu.fer.unizg.msins.chatty.networking.services

import edu.fer.unizg.msins.chatty.networking.API
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Url

@Deprecated(message = "Soon deprecated by Twitch")
interface KrakenService {
    @Headers("Client-ID: ${API.CLIENT_ID}")
    suspend fun getProfileInfo(
        @Header("Authorization") oAuth: String,
        @Url url: String
    )
}