package pl.piter.conversation.domain.model

import java.time.LocalDateTime

data class ConversationDateTime(val createdAt: LocalDateTime) {

    companion object {
        fun now() = ConversationDateTime(LocalDateTime.now())
    }
}
