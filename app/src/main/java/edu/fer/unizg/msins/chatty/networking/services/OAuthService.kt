package edu.fer.unizg.msins.chatty.networking.services

import edu.fer.unizg.msins.chatty.networking.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface OAuthService {
    companion object {
        const val BASE_URL = "https://id.twitch.tv/oauth2/validate"
    }

    @GET
    suspend fun getValidatedUser(
        @Header("Authorization") oAuth: String,
        @Url url: String
    ): Response<UserDto>

}