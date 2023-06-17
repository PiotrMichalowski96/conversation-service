package pl.piter.conversation.adapter.api.model

data class MessageResponse(
    val id: String,
    val author: MessageAuthorResponse,
    val content: String,
)