package pl.piter.conversation.adapter.chatgpt.mapper

import pl.piter.conversation.adapter.chatgpt.model.*
import pl.piter.conversation.domain.model.*

private const val CHAT_GPT_MODEL = "gpt-3.5-turbo"

fun ChatGPTResponse.toDomain(): Message {
    require(choices.isNotEmpty())
    return Message (
        messageId = MessageId.random(),
        messageAuthor = mapAuthor(choices[0].message.role),
        messageContent = MessageContent(choices[0].message.content)
    )
}

fun Conversation.toChatRequest() = ChatGPTRequest(
    model = CHAT_GPT_MODEL,
    messages = mapChatGPTMessages(conversationMessages.messages)
)

private fun mapAuthor(role: Role) = when(role) {
    Role.user -> MessageAuthor.USER
    else -> MessageAuthor.CHAT_GPT
}

private fun mapChatGPTMessages(messages: MutableList<Message>): List<ChatGPTMessage> =
    messages.map { it.toChatGPTMessage() }

private fun Message.toChatGPTMessage() = ChatGPTMessage(
    role = mapRole(messageAuthor),
    content = messageContent.content
)

private fun mapRole(messageAuthor: MessageAuthor) = when(messageAuthor) {
    MessageAuthor.CHAT_GPT -> Role.assistant
    MessageAuthor.USER -> Role.user
}
