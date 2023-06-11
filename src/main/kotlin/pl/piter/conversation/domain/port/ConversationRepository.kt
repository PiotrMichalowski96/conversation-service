package pl.piter.conversation.domain.port

import pl.piter.conversation.domain.model.Conversation
import pl.piter.conversation.domain.model.ConversationId
import pl.piter.conversation.domain.model.UserId

interface ConversationRepository {

    fun findById(conversationId: ConversationId): Conversation?

    fun findByUserId(userId: UserId): List<Conversation>

    fun saveOrUpdate(conversation: Conversation): Conversation

    fun delete(conversationId: ConversationId)
}