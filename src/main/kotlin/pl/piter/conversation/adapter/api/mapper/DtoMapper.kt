package pl.piter.conversation.adapter.api.mapper

import pl.piter.conversation.adapter.api.model.ConversationResponse
import pl.piter.conversation.adapter.api.model.MessageAuthorResponse
import pl.piter.conversation.adapter.api.model.MessageRequest
import pl.piter.conversation.adapter.api.model.MessageResponse
import pl.piter.conversation.domain.model.*

fun Conversation.toResponse() = ConversationResponse(
    id = conversationId.id,
    name = conversationName.name,
    createdAt = conversationDateTime.createdAt,
    messages = conversationMessages.messages.map { it.toResponse() }
)

fun Message.toResponse() = MessageResponse(
    id = messageId.id,
    author = mapMessageAuthorResponse(messageAuthor),
    content = messageContent.content
)

fun MessageRequest.toDomain() = Message(
    messageId = MessageId.random(),
    messageAuthor = MessageAuthor.USER,
    messageContent = MessageContent(content)
)

private fun mapMessageAuthorResponse(messageAuthor: MessageAuthor): MessageAuthorResponse = when(messageAuthor) {
    MessageAuthor.USER -> MessageAuthorResponse.USER
    MessageAuthor.CHAT_GPT -> MessageAuthorResponse.CHAT_GPT
}