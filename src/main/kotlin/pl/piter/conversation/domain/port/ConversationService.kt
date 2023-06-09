package pl.piter.conversation.domain.port

import pl.piter.conversation.domain.model.*

interface ConversationService {

    fun get(conversationId: ConversationId): Conversation

    fun delete(conversationId: ConversationId)

    fun initiateConversation(userId: UserId, name: ConversationName): ConversationId

    fun addMessage(message: Message)

    fun removeMessage(message: Message)
}