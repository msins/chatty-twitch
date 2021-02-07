package edu.fer.unizg.msins.chatty.parsing

data class MessageContext(
    val user: TwitchUser,
    val text: String,
    val channel: String,
    val elements: List<Token>
)