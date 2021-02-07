package edu.fer.unizg.msins.chatty.networking.services

import edu.fer.unizg.msins.chatty.emotes.services.ffz.FfzChannelInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FfzService {
    @GET("room/{name}")
    suspend fun getChannelInfo(@Path("name") name: String): Response<FfzChannelInfo>
}