package edu.fer.unizg.msins.chatty.login

data class User(var oAuth: String, var id: String, var name: String) {
    companion object {
        /**
         * justinfanXXX is twitch official anonymous name
         */
        val ANONYMOUS: User = User("", "", "justinfan123")
        private var current: User = ANONYMOUS

        fun getCurrent(): User {
            return current
        }

        fun setCurrent(user: User) {
            current = user
        }

        fun isAnonymous(): Boolean {
            return current == ANONYMOUS
        }
    }
}