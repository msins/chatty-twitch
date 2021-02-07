package edu.fer.unizg.msins.chatty.networking

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object Retrofits {
    val ffz: Retrofit by lazy {
        create(API.FFZ_BASE_URL)
    }

    val badges: Retrofit by lazy {
        create(API.BADGE_SERVICE_BASE_URL)
    }

    val twitchEmotes: Retrofit by lazy {
        create(API.TWITCH_EMOTES_SERVICE_BASE_URL)
    }

    val oauth: Retrofit by lazy {
        //base url doesn't matter here
        create(API.HELIX_BASE_URL)
    }

    val kraken: Retrofit by lazy {
        create(API.KRAKEN_BASE_URL)
    }

    val bttv: Retrofit by lazy {
        create(API.BTTV_BASE_URL)
    }

    val helix: Retrofit by lazy {
        create(API.HELIX_BASE_URL)
    }

    private val create: (String) -> Retrofit = { baseUrl: String ->
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            ).build()
    }

    private val pool = ConnectionPool(5, 15000, TimeUnit.MILLISECONDS)
    private val client = OkHttpClient.Builder()
        .connectionPool(pool)
        .build()
}