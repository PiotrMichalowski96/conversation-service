package pl.piter.conversation.adapter.chatgpt.mapper

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import pl.piter.conversation.adapter.chatgpt.model.ChatGPTRequest
import pl.piter.conversation.adapter.chatgpt.model.ChatGPTResponse
import pl.piter.conversation.domain.model.Conversation
import pl.piter.conversation.domain.model.Message
import pl.piter.conversation.util.JsonConverter

class ChatMapperTest {

    @Test
    fun `given ChatGPT response when mapped then return message`() {
        //given
        val chatGPTResponseSample = "src/test/resources/responseChatGPT_2.json"
        val chatGPTResponse: ChatGPTResponse = JsonConverter.readJsonFile(chatGPTResponseSample)

        val expectedMessageSample = "src/test/resources/messageDomain_2.json"
        val expectedMessage: Message = JsonConverter.readJsonFile(expectedMessageSample)

        //when
        val actualMessage: Message = chatGPTResponse.toDomain()

        //then
        assertThat(actualMessage)
            .usingRecursiveComparison()
            .ignoringFields("messageId.id")
            .isEqualTo(expectedMessage)
    }

    @Test
    fun `given invalid ChatGPT response when mapped then throws exception`() {
        //given
        val invalidSample = "src/test/resources/responseChatGPT_invalid.json"
        val invalidChatGPTResponse: ChatGPTResponse = JsonConverter.readJsonFile(invalidSample)

        //whenThen
        assertThatThrownBy { invalidChatGPTResponse.toDomain() }.isInstanceOf(
            IllegalArgumentException::class.java
        )
    }

    @Test
    fun `given conversation when mapped then return ChatGPT request`() {
        //given
        val conversationSample = "src/test/resources/conversationDomain_4.json"
        val conversation: Conversation = JsonConverter.readJsonFile(conversationSample)

        val expectedChatGPTRequestSample = "src/test/resources/requestChatGPT_4.json"
        val expectedChatGPTRequest: ChatGPTRequest = JsonConverter.readJsonFile(expectedChatGPTRequestSample)

        //when
        val actualChatGPTRequest: ChatGPTRequest = conversation.toChatRequest()

        //then
        assertThat(actualChatGPTRequest).isEqualTo(expectedChatGPTRequest)
    }
}