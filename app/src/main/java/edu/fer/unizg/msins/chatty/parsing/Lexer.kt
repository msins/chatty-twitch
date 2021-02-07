package edu.fer.unizg.msins.chatty.parsing

import java.util.concurrent.ConcurrentHashMap
import java.util.function.Predicate

typealias WordHandler = (word: String, start: Int, end: Int) -> Token

class Lexer {
    private val handlers: HashMap<String, WordHandler> = HashMap()

    companion object {
        private val isWhitespace: Predicate<Char> = Predicate(Char::isWhitespace)
    }

    fun addWordHandler(words: Iterable<String>, handler: WordHandler) {
        words.forEach { word -> handlers[word] = handler }
    }

    fun lex(message: CharArray): List<Token> {
        var index = 0
        val elements = ArrayList<Token>()
        while (true) {
            index = skipBlanks(message, index)
            val start = index
            index = skipWord(message, index)
            if (start == index) {
                break
            }

            val word = String(message, start, index - start)
            if (handlers.containsKey(word)) {
                elements.add(handlers[word]!!(word, start, index))
                continue
            }
            elements.add(Token(Token.Type.WORD, word, start, index))
        }
        return elements
    }

    private fun skip(chars: CharArray, index: Int, predicate: Predicate<Char>): Int {
        var newIndex = index
        while (isValidIndex(chars, newIndex) && predicate.test(chars[newIndex])) {
            newIndex++
        }
        return newIndex
    }

    private fun isValidIndex(chars: CharArray, index: Int): Boolean {
        return index >= 0 && index < chars.size
    }

    private fun skipBlanks(chars: CharArray, index: Int): Int {
        return skip(chars, index, isWhitespace)
    }

    private fun skipWord(chars: CharArray, index: Int): Int {
        return skip(chars, index, isWhitespace.negate())
    }
}