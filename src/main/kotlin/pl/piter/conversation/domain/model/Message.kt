package pl.piter.conversation.domain.model

data class Message(
    val messageId: MessageId,
    val messageAuthor: MessageAuthor,
    val messageContent: MessageContent,
)
