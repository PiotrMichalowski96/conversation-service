package pl.piter.conversation.adapter.mongo

import org.springframework.stereotype.Repository
import pl.piter.conversation.adapter.mongo.mapper.toDbModel
import pl.piter.conversation.adapter.mongo.mapper.toDomain
import pl.piter.conversation.domain.model.Conversation
import pl.piter.conversation.domain.model.ConversationId
import pl.piter.conversation.domain.model.Username
import pl.piter.conversation.domain.port.ConversationRepository

@Repository
class MongoDbConversationRepository(private val repository: SpringConversationRepository) :
    ConversationRepository {

    override fun findByIdAndUsername(conversationId: ConversationId, username: Username): Conversation? {
        return repository.findByIdAndUsername(conversationId.id, username.name)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findByUsername(username: Username): List<Conversation> {
        return repository.findByUsername(username.name)
            .map { it.toDomain() }
    }

    override fun saveOrUpdate(conversation: Conversation): Conversation {
        return repository.save(conversation.toDbModel())
            .toDomain()
    }

    override fun delete(conversationId: ConversationId, username: Username) {
        repository.deleteByIdAndUsername(conversationId.id, username.name)
    }
}