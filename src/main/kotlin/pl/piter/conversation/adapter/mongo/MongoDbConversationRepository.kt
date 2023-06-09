package pl.piter.conversation.adapter.mongo

import org.springframework.stereotype.Repository
import pl.piter.conversation.adapter.mongo.mapper.toDbModel
import pl.piter.conversation.adapter.mongo.mapper.toDomain
import pl.piter.conversation.domain.model.Conversation
import pl.piter.conversation.domain.model.ConversationId
import pl.piter.conversation.domain.model.UserId
import pl.piter.conversation.domain.port.ConversationRepository

@Repository
class MongoDbConversationRepository(private val repository: SpringConversationRepository) :
    ConversationRepository {

    override fun findById(conversationId: ConversationId): Conversation? {
        return repository.findById(conversationId.id)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findByUserId(userId: UserId): List<Conversation> {
        return repository.findByUserId(userId.id)
            .map { it.toDomain() }
    }

    override fun saveOrUpdate(conversation: Conversation): Conversation {
        return repository.save(conversation.toDbModel())
            .toDomain()
    }

    override fun delete(conversationId: ConversationId) {
        repository.deleteById(conversationId.id)
    }
}