package edu.fer.unizg.msins.chatty.parsing

data class Token(
    var type: Type,
    var value: String,
    var start: Int,
    var end: Int
) {
    enum class Type {
        WORD,
        EMOTE,
        GIF
    }
}