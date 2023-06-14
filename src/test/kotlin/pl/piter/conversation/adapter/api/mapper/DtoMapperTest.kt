package pl.piter.conversation.adapter.api.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.piter.conversation.adapter.api.model.ConversationResponse
import pl.piter.conversation.adapter.api.model.MessageRequest
import pl.piter.conversation.adapter.api.model.MessageResponse
import pl.piter.conversation.domain.model.Conversation
import pl.piter.conversation.domain.model.Message
import pl.piter.conversation.util.JsonConverter

class DtoMapperTest {

    @Test
    fun `given conversation when mapped then return conversation response`() {
        //given
        val conversationSample = "src/test/resources/conversationDomain.json"
        val conversation: Conversation = JsonConverter.readJsonFile(conversationSample)

        val expectedResponseSample = "src/test/resources/conversationResponse.json"
        val expectedResponse: ConversationResponse = JsonConverter.readJsonFile(expectedResponseSample)

        //when
        val actualResponse: ConversationResponse = conversation.toResponse()

        //then
        assertThat(actualResponse).isEqualTo(expectedResponse)
    }

    @Test
    fun `given message when mapped then return message response`() {
        //given
        val messageSample = "src/test/resources/messageDomain_2.json"
        val message: Message = JsonConverter.readJsonFile(messageSample)

        val expectedResponseSample = "src/test/resources/messageResponse_2.json"
        val expectedResponse: MessageResponse = JsonConverter.readJsonFile(expectedResponseSample)

        //when
        val actualResponse: MessageResponse = message.toResponse()

        //then
        assertThat(actualResponse).isEqualTo(expectedResponse)
    }

    @Test
    fun `given message request when mapped then return message domain`() {
        //given
        val messageRequestSample = "src/test/resources/messageRequest_2.json"
        val messageRequest: MessageRequest = JsonConverter.readJsonFile(messageRequestSample)

        val expectedMessageSample = "src/test/resources/messageDomain_2.json"
        val expectedMessage: Message = JsonConverter.readJsonFile(expectedMessageSample)

        //when
        val actualMessage: Message = messageRequest.toDomain()

        //then
        assertThat(actualMessage.messageContent).isEqualTo(expectedMessage.messageContent)
    }
}