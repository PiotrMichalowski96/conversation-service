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

fun Conversation.toDbModel() = ConversationDbModel(
    id = conversationId.id,
    userId = userId.id,
    name = conversationName.name,
    createdAt = conversationDateTime.createdAt,
    messages = conversationMessages.messages.map { it.toDbModel() }
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

private fun Message.toDbModel() = MessageDbModel(
    id = messageId.id,
    content = messageContent.content,
    author = mapAuthor(messageAuthor)
)

private fun mapAuthor(authorDomain: MessageAuthor) = when (authorDomain) {
    MessageAuthor.USER -> AuthorDbModel.USER
    MessageAuthor.CHAT_GPT -> AuthorDbModel.CHAT_GPT
}