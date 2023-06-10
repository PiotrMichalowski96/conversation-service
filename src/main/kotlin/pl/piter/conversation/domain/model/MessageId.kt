package pl.piter.conversation.domain.model

import java.util.*

data class MessageId(val id: String) {
    companion object {
        fun random() = MessageId(UUID.randomUUID().toString())
    }
}
