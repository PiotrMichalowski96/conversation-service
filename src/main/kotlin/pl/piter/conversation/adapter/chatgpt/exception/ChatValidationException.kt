package pl.piter.conversation.adapter.chatgpt.exception

import org.springframework.validation.Errors

class ChatValidationException(message: String) : RuntimeException(message) {

    companion object {
        fun of(errors: Errors) = ChatValidationException("Not valid object: ${errors.allErrors}")
    }
}