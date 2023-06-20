package pl.piter.conversation.adapter.mongo.model

import java.time.LocalDateTime

data class ConversationDbModel(
    val id: String,
    val username: String,
    val name: String,
    val createdAt: LocalDateTime,
    val messages: List<MessageDbModel>,
)
