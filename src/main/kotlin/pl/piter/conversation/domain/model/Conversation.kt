package pl.piter.conversation.domain.model

import pl.piter.conversation.domain.exception.DomainException

data class Conversation private constructor(
    val id: ConversationId = ConversationId.random(),
    val userId: UserId,
    val name: ConversationName,
    val conversationMessages: ConversationMessages = ConversationMessages(),
    val createdAt: ConversationDateTime = ConversationDateTime.now(),
) {

    companion object {
        fun initiate(userId: UserId, name: ConversationName): Conversation {
            return Conversation(userId = userId, name = name)
        }
    }

    fun addMessage(message: Message) {
        conversationMessages.messages.add(message)
    }

    fun removeMessage(message: Message) {
        validateAuthor(message)
        conversationMessages.messages.remove(message)
    }

    private fun validateAuthor(message: Message) {
        if (message.author != MessageAuthor.USER) {
            throw DomainException("Author of message to remove need to be a user")
        }
    }
}
