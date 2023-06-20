package pl.piter.conversation.domain.port

import pl.piter.conversation.domain.model.Conversation
import pl.piter.conversation.domain.model.ConversationId
import pl.piter.conversation.domain.model.Username

interface ConversationRepository {

    fun findByIdAndUsername(conversationId: ConversationId, username: Username): Conversation?

    fun findByUsername(username: Username): List<Conversation>

    fun saveOrUpdate(conversation: Conversation): Conversation

    fun delete(conversationId: ConversationId, username: Username)
}