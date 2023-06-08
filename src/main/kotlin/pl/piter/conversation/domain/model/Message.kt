package pl.piter.conversation.domain.model

data class Message(
    val id: MessageId,
    val author: MessageAuthor,
    val content: MessageContent,
)
