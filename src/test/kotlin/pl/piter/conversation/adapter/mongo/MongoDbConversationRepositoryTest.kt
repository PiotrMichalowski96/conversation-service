package pl.piter.conversation.adapter.mongo

import org.apache.commons.lang3.RandomStringUtils
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
import pl.piter.conversation.domain.model.ConversationId
import pl.piter.conversation.domain.model.ConversationName
import pl.piter.conversation.domain.model.Username
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
    fun `given conversation id and username when find by id and username then return conversation`() {
        //given
        val sampleNo = 0
        val expectedConversation: Conversation = ConversationTestData.getDomain(sampleNo)
        val conversationId = expectedConversation.conversationId
        val username = expectedConversation.username

        //when
        val actualConversation = mongoDbConversationRepository.findByIdAndUsername(conversationId, username)

        //then
        assertThat(actualConversation).isEqualTo(expectedConversation)
    }

    @Test
    fun `given non-existing conversation id and username when find by id then return null`() {
        //given
        val nonExistingId = ConversationId(UUID.randomUUID().toString())
        val username: Username = ConversationTestData.getDomain(0).username

        //when
        val conversation = mongoDbConversationRepository.findByIdAndUsername(nonExistingId, username)

        //then
        assertThat(conversation).isNull()
    }

    @Test
    fun `given conversation id and wrong username when find by id then return null`() {
        //given
        val conversationId = ConversationTestData.getDomain(0).conversationId
        val username = Username(RandomStringUtils.randomAlphanumeric(10))

        //when
        val conversation = mongoDbConversationRepository.findByIdAndUsername(conversationId, username)

        //then
        assertThat(conversation).isNull()
    }

    @Test
    fun `given username when find by username then return conversation`() {
        //given
        val sampleNo = 0
        val expectedConversations: List<Conversation> = listOf(ConversationTestData.getDomain(sampleNo))
        val username = expectedConversations[0].username

        //when
        val actualConversation = mongoDbConversationRepository.findByUsername(username)

        //then
        assertThat(actualConversation)
            .hasSize(1)
            .hasSameElementsAs(expectedConversations)
    }

    @Test
    fun `given non-existing user id when find by user id then return empty list`() {
        //given
        val nonExistingUsername = Username(RandomStringUtils.randomAlphanumeric(10))

        //when
        val conversation = mongoDbConversationRepository.findByUsername(nonExistingUsername)

        //then
        assertThat(conversation).isEmpty()
    }

    @Test
    fun `given conversation when save then it is persistent in MongoDB`() {
        //given
        val username = Username(RandomStringUtils.randomAlphanumeric(10))
        val conversation = Conversation.initiate(username, ConversationName("name"))

        //when
        val savedConversation = mongoDbConversationRepository.saveOrUpdate(conversation)
        val foundConversation = mongoDbConversationRepository.findByIdAndUsername(conversation.conversationId, username)

        //then
        assertThat(savedConversation).isEqualTo(conversation)
        assertThat(foundConversation).usingRecursiveComparison()
            .ignoringFields("conversationDateTime.createdAt")
            .isEqualTo(conversation)
    }

    @Test
    fun `given conversation id and username when delete then it is not present in MongoDB`() {
        //given
        val sampleNo = 1
        val conversationToBeRemoved: Conversation = ConversationTestData.getDomain(sampleNo)
        val conversationId = conversationToBeRemoved.conversationId
        val username = conversationToBeRemoved.username

        //when
        mongoDbConversationRepository.delete(conversationId, username)
        val foundConversation = mongoDbConversationRepository.findByIdAndUsername(conversationId, username)

        //then
        assertThat(foundConversation).isNull()
    }
}