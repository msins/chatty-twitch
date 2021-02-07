package edu.fer.unizg.msins.chatty.networking.services

import edu.fer.unizg.msins.chatty.networking.dto.BadgesSetDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BadgeService {
    @GET("{channelId}/display")
    suspend fun getSubBadges(@Path("channelId") channelId: String): Response<BadgesSetDto>
}