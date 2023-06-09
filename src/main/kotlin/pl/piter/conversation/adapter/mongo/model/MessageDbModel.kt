package pl.piter.conversation.adapter.mongo.model

data class MessageDbModel(
    val id: String,
    val content: String,
    val author: AuthorDbModel,
)

enum class AuthorDbModel {
    USER, CHAT_GPT
}