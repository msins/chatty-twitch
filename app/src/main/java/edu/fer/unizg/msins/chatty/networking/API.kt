package edu.fer.unizg.msins.chatty.networking

object API {
    const val TWITCH_EMOTES_CDN_BASE_URL = "https://static-cdn.jtvnw.net/emoticons/v1/"
    const val FFZ_BASE_URL = "https://api.frankerfacez.com/v1/"
    const val KRAKEN_BASE_URL = "https://api.twitch.tv/kraken/user"
    const val HELIX_BASE_URL = "https://api.twitch.tv/helix/"
    const val BTTV_BASE_URL = "https://api.betterttv.net/3/cached/"
    const val BTTV_CDN_BASE_URL = "https://cdn.betterttv.net/emote/"
    const val BADGE_SERVICE_BASE_URL = "https://badges.twitch.tv/v1/badges/channels/"
    const val TWITCH_EMOTES_SERVICE_BASE_URL = "https://api.twitchemotes.com/api/v4/"
    const val CLIENT_ID = "xhn5gu5agnaeil9sn6etmp406xty5z"
    const val REDIRECT = "http://localhost"
    const val BASE = "https://id.twitch.tv/oauth2/authorize?response_type=token"
    const val SCOPE = "chat:edit+chat:read+user_read+user_subscriptions+whispers:read+whispers:edit"
    const val TWITCH = "$BASE&client_id=$CLIENT_ID&redirect_uri=$REDIRECT&scope=$SCOPE"
}