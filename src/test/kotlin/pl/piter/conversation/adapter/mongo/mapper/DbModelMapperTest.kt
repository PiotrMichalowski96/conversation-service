package pl.piter.conversation.adapter.mongo.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.piter.conversation.adapter.mongo.model.ConversationDbModel
import pl.piter.conversation.domain.model.Conversation
import pl.piter.conversation.util.ConversationTestData

class DbModelMapperTest {

    @Test
    fun `given db model when mapped then return domain model`() {
        //given
        val sampleNo = 0
        val conversationDb: ConversationDbModel = ConversationTestData.getDatabaseModel(sampleNo)
        val expectedConversationDomain: Conversation = ConversationTestData.getDomain(sampleNo)

        //when
        val actualConversationDomain: Conversation = conversationDb.toDomain()

        //then
        assertThat(actualConversationDomain).isEqualTo(expectedConversationDomain)
    }

    @Test
    fun `given domain when mapped then return db model`() {
        //given
        val sampleNo = 1
        val conversationDomain: Conversation = ConversationTestData.getDomain(sampleNo)
        val expectedConversationDb: ConversationDbModel = ConversationTestData.getDatabaseModel(sampleNo)

        //when
        val actualConversationDb: ConversationDbModel = conversationDomain.toDbModel()

        //then
        assertThat(actualConversationDb).isEqualTo(expectedConversationDb)
    }
}