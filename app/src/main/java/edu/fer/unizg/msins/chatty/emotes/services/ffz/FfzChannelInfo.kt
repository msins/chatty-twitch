package edu.fer.unizg.msins.chatty.emotes.services.ffz

import com.squareup.moshi.Json

class FfzChannelInfo private constructor(
    private val room: Room,
    /**
     * There is always only one set but because of json representation has to be Map.
     */
    private val sets: Map<String, EmoteSet>
) {

    fun getEmoteDescriptors(): List<FfzEmoteDescriptor> {
        val emotes = ArrayList<FfzEmoteDescriptor>(sets.size)
        for (e in sets.values.flatMap { e -> e.emoticons }) {
            emotes.add(e)
        }
        return emotes
    }

    private class EmoteSet(val emoticons: List<FfzEmoteDescriptor>) {
        override fun toString(): String {
            return emoticons.toString()
        }

        fun getEmoteNames(): List<String> {
            return emoticons.map { e -> e.name }
        }
    }

    class Room(
        @Json(name = "_id") var _id: Int,
        @Json(name = "display_name") var displayName: String,
        @Json(name = "id") var id: String,
        @Json(name = "set") var setId: Int,
        @Json(name = "twitch_id") var twitchId: Int
    ) {
        override fun toString(): String {
            return "Room(_id=$_id, displayName='$displayName', id='$id', setId=$setId, twitchId=$twitchId)"
        }
    }

}