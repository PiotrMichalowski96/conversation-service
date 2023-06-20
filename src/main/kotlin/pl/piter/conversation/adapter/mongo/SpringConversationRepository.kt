package pl.piter.conversation.adapter.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import pl.piter.conversation.adapter.mongo.model.ConversationDbModel
import java.util.Optional

interface SpringConversationRepository: MongoRepository<ConversationDbModel, String> {

    fun findByIdAndUsername(id: String, username: String): Optional<ConversationDbModel>

    fun findByUsername(username: String): List<ConversationDbModel>

    fun deleteByIdAndUsername(id: String, username: String)
}