package pl.piter.conversation.domain.model

import java.util.*

data class ConversationId(val id: String) {

    companion object {
        fun random() = ConversationId(UUID.randomUUID().toString())
    }
}
