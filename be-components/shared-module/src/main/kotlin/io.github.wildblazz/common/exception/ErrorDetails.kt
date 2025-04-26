package io.github.wildblazz.common.exception

class ErrorDetails {
    private var messages: MutableList<String?> = ArrayList()

    constructor(message: String?) {
        messages.add(message)
    }

    constructor(messages: List<String?>?) {
        if (messages != null) {
            this.messages.addAll(messages)
        }
    }

    fun getMessages(): List<String?> {
        return this.messages
    }

    fun setMessages(messages: MutableList<String?>) {
        this.messages = messages
    }
}
