package pl.piter.conversation.domain.model

data class Conversation(
    val id: ConversationId,
    val userId: UserId,
    val name: ConversationName,
    val messages: ConversationMessages,
    val createdAt: ConversationDateTime,
)
