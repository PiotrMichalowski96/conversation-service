package pl.piter.conversation.repository

import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository
import pl.piter.conversation.entity.Conversation

@EnableScan
interface ConversationRepository: CrudRepository<Conversation, String> {

    fun findByUserIdOrderByCreatedAtDesc(userId: String): List<Conversation>
}

