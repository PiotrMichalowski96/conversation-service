package pl.piter.conversation.adapter.chatgpt

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import pl.piter.conversation.adapter.chatgpt.client.ChatGPTApiClient
import pl.piter.conversation.adapter.chatgpt.exception.ChatValidationException
import pl.piter.conversation.adapter.chatgpt.model.ChatGPTRequest
import pl.piter.conversation.adapter.chatgpt.model.ChatGPTResponse
import pl.piter.conversation.adapter.chatgpt.validator.AnnotationValidator
import pl.piter.conversation.domain.model.Conversation
import pl.piter.conversation.domain.model.Message
import pl.piter.conversation.util.JsonConverter

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [ChatGPTService::class, AnnotationValidator::class, LocalValidatorFactoryBean::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatGPTServiceTest {

    @Autowired
    private lateinit var chatGPTService: ChatGPTService

    @MockkBean
    private lateinit var chatApiClient: ChatGPTApiClient

    @Test
    fun `given conversation when ask chat then return message`() {
        //given
        val conversationSample = "src/test/resources/conversationDomain_4.json"
        val conversationWithQuestion: Conversation = JsonConverter.readJsonFile(conversationSample)

        val requestSample = "src/test/resources/requestChatGPT_4.json"
        val request: ChatGPTRequest = JsonConverter.readJsonFile(requestSample)

        val responseSample = "src/test/resources/responseChatGPT_2.json"
        val response: ChatGPTResponse = JsonConverter.readJsonFile(responseSample)

        val expectedMessageSample = "src/test/resources/messageDomain_2.json"
        val expectedMessage: Message = JsonConverter.readJsonFile(expectedMessageSample)

        every { chatApiClient.askChat(request) } returns response

        //when
        val actualMessage: Message = chatGPTService.ask(conversationWithQuestion)

        //then
        assertThat(actualMessage)
            .usingRecursiveComparison()
            .ignoringFields("messageId.id")
            .isEqualTo(expectedMessage)
    }

    @Test
    fun `given conversation when ask chat with invalid request then throw exception`() {
        //given
        val invalidConversationSample = "src/test/resources/conversationDomain_invalid.json"
        val invalidConversation: Conversation = JsonConverter.readJsonFile(invalidConversationSample)

        //whenThen
        assertThatThrownBy { chatGPTService.ask(invalidConversation) }
            .isInstanceOf(ChatValidationException::class.java)
    }
}