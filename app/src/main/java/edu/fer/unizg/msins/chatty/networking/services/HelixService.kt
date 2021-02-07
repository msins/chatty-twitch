package edu.fer.unizg.msins.chatty.networking.services

import edu.fer.unizg.msins.chatty.networking.API
import edu.fer.unizg.msins.chatty.networking.dto.TwitchUsersInfoDto
import edu.fer.unizg.msins.chatty.networking.dto.UserFollowsDto
import retrofit2.Response
import retrofit2.http.*

interface HelixService {
    @GET("users")
    @Headers("Client-Id: ${API.CLIENT_ID}")
    suspend fun getUserInfo(
        @Header("Authorization") oAuth: String,
        @Query("login") id: String
    ): Response<TwitchUsersInfoDto>

    @GET("users/follows")
    @Headers("Client-Id: ${API.CLIENT_ID}")
    suspend fun getUserFollows(
        @Header("Authorization") oAuth: String,
        @Query("from_id") id: String
    ): Response<UserFollowsDto>

    @GET("users/follows")
    @Headers("Client-Id: ${API.CLIENT_ID}")
    suspend fun getUserFollowers(
        @Header("Authorization") oAuth: String,
        @Query("to_id") id: String
    ): Response<UserFollowsDto>
}