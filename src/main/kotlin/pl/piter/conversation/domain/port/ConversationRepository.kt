package pl.piter.conversation.domain.port

import pl.piter.conversation.domain.model.Conversation
import pl.piter.conversation.domain.model.ConversationId
import pl.piter.conversation.domain.model.UserId

interface ConversationRepository {

    fun findById(conversationId: ConversationId): Conversation

    fun findByUserId(userId: UserId): Conversation

    fun save(conversation: Conversation)

    fun update(conversation: Conversation)

    fun delete(conversationId: ConversationId)
}