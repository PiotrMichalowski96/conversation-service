package pl.piter.conversation.adapter.mongo.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.piter.conversation.adapter.mongo.model.ConversationDbModel
import pl.piter.conversation.domain.model.Conversation
import pl.piter.conversation.util.JsonConverter

class DbModelMapperTest {

    @Test
    fun `given db model when mapped then return domain model`() {
        //given
        val conversationDbSample = "src/test/resources/conversationDbModel.json"
        val conversationDb: ConversationDbModel = JsonConverter.readJsonFile(conversationDbSample)

        val expectedConversationSample = "src/test/resources/conversationDomain.json"
        val expectedConversationDomain: Conversation = JsonConverter.readJsonFile(expectedConversationSample)

        //when
        val actualConversationDomain: Conversation = conversationDb.toDomain()

        //then
        assertThat(actualConversationDomain).isEqualTo(expectedConversationDomain)
    }
}