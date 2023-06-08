package pl.piter.conversation.adapter.mongo.mapper

import pl.piter.conversation.adapter.mongo.model.AuthorDbModel
import pl.piter.conversation.adapter.mongo.model.ConversationDbModel
import pl.piter.conversation.adapter.mongo.model.MessageDbModel
import pl.piter.conversation.domain.model.*

fun ConversationDbModel.toDomain() = Conversation(
    conversationId = ConversationId(id),
    userId = UserId(userId),
    conversationName = ConversationName(name),
    conversationMessages = ConversationMessages(messages.map { it.toDomain() }.toMutableList()),
    conversationDateTime = ConversationDateTime(createdAt)
)

private fun MessageDbModel.toDomain() = Message(
    messageId = MessageId(id),
    messageAuthor = mapAuthor(author),
    messageContent = MessageContent(content)
)

private fun mapAuthor(authorDbModel: AuthorDbModel) = when (authorDbModel) {
    AuthorDbModel.USER -> MessageAuthor.USER
    AuthorDbModel.CHAT_GPT -> MessageAuthor.CHAT_GPT
}