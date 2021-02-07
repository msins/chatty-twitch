package edu.fer.unizg.msins.chatty.parsing

object WordHandlers {
    val EMOTE : WordHandler = { word, start, end -> Token(Token.Type.EMOTE, word, start, end) }
    val GIF : WordHandler = { word, start, end -> Token(Token.Type.GIF, word, start, end) }
}