package pl.piter.conversation.adapter.api.model

import java.time.LocalDateTime

data class ConversationResponse(
    val id: String,
    val name: String,
    val createdAt: LocalDateTime,
    val messages: List<MessageResponse>,
)
