package pl.piter.conversation.adapter.mongo.model

import java.time.LocalDateTime

data class ConversationDbModel(
    val id: String,
    val userId: String,
    val name: String,
    val createdAt: LocalDateTime,
    val messages: List<MessageDbModel>,
)
