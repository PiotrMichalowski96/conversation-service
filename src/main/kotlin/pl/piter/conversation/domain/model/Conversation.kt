package pl.piter.conversation.domain.model

import pl.piter.conversation.domain.exception.DomainException

data class Conversation(
    val conversationId: ConversationId = ConversationId.random(),
    val userId: UserId,
    val conversationName: ConversationName,
    val conversationMessages: ConversationMessages = ConversationMessages(),
    val conversationDateTime: ConversationDateTime = ConversationDateTime.now(),
) {

    companion object {
        fun initiate(userId: UserId, name: ConversationName): Conversation {
            return Conversation(userId = userId, conversationName = name)
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
        if (message.messageAuthor != MessageAuthor.USER) {
            throw DomainException("Author of message to remove need to be a user")
        }
    }
}
