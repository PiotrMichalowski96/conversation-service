package pl.piter.conversation.adapter.mongo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import pl.piter.conversation.adapter.mongo.model.ConversationDbModel
import pl.piter.conversation.domain.model.Conversation
import pl.piter.conversation.domain.model.ConversationName
import pl.piter.conversation.domain.model.UserId
import pl.piter.conversation.util.ConversationTestData
import java.util.*

@DataMongoTest
@ActiveProfiles("TEST")
@Import(MongoDbConversationRepository::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MongoDbConversationRepositoryTest {

    @Autowired
    private lateinit var springConversationRepository: SpringConversationRepository

    @Autowired
    private lateinit var mongoDbConversationRepository: MongoDbConversationRepository

    @BeforeAll
    fun initDatabase() {
        val conversations: List<ConversationDbModel> = ConversationTestData.getDatabaseModels()
        springConversationRepository.saveAll(conversations)
    }

    @Test
    fun `given conversation id when find by id then return conversation`() {
        //given
        val sampleNo = 0
        val expectedConversation: Conversation = ConversationTestData.getDomain(sampleNo)
        val conversationId = expectedConversation.conversationId

        //when
        val actualConversation = mongoDbConversationRepository.findById(conversationId)

        //then
        assertThat(actualConversation).isEqualTo(expectedConversation)
    }

    @Test
    fun `given user id when find by user id then return conversation`() {
        //given
        val sampleNo = 0
        val expectedConversation: Conversation = ConversationTestData.getDomain(sampleNo)
        val userId = expectedConversation.userId

        //when
        val actualConversation = mongoDbConversationRepository.findByUserId(userId)

        //then
        assertThat(actualConversation).isEqualTo(expectedConversation)
    }

    @Test
    fun `given non-existing user id when find by user id then return null`() {
        //given
        val nonExistingUserId = UserId(UUID.randomUUID().toString())

        //when
        val conversation = mongoDbConversationRepository.findByUserId(nonExistingUserId)

        //then
        assertThat(conversation).isNull()
    }

    @Test
    fun `given conversation when save then it is persistent in MongoDB`() {
        //given
        val conversation = Conversation.initiate(
            UserId(UUID.randomUUID().toString()),
            ConversationName("Some conversation name")
        )

        //when
        val savedConversation = mongoDbConversationRepository.saveOrUpdate(conversation)
        val foundConversation = mongoDbConversationRepository.findById(conversation.conversationId)

        //then
        assertThat(savedConversation).isEqualTo(conversation)
        assertThat(foundConversation).usingRecursiveComparison()
            .ignoringFields("conversationDateTime.createdAt")
            .isEqualTo(conversation)
    }

    @Test
    fun `given conversation id when delete then it is not present in MongoDB`() {
        //given
        val sampleNo = 1
        val conversationToBeRemoved: Conversation = ConversationTestData.getDomain(sampleNo)

        //when
        mongoDbConversationRepository.delete(conversationToBeRemoved.conversationId)
        val foundConversation = mongoDbConversationRepository.findById(conversationToBeRemoved.conversationId)

        //then
        assertThat(foundConversation).isNull()
    }
}