package pl.piter.conversation.adapter.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import pl.piter.conversation.adapter.mongo.model.ConversationDbModel
import java.util.*

interface SpringConversationRepository: MongoRepository<ConversationDbModel, String> {

    fun findByUserId(userId: String): Optional<ConversationDbModel>
}