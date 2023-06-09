package pl.piter.conversation.adapter.chatgpt.client

import com.ninjasquad.springmockk.MockkBean
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import pl.piter.conversation.adapter.chatgpt.config.FeignConfig
import pl.piter.conversation.adapter.chatgpt.model.ChatGPTRequest
import pl.piter.conversation.adapter.chatgpt.model.ChatGPTResponse
import pl.piter.conversation.adapter.mongo.SpringConversationRepository
import pl.piter.conversation.util.JsonConverter

@EnabledIfEnvironmentVariable(
    named = "chat-gpt-api_key",
    matches = "^(?=\\s*\\S).*$",
    disabledReason = "API Key must not be blank"
)
@SpringBootTest
@ActiveProfiles("TEST")
@Import(FeignConfig::class)
class ChatGPTApiClientTest(@Autowired private val apiClient: ChatGPTApiClient) {

    @MockkBean
    private lateinit var repository: SpringConversationRepository

    @Test
    fun `given chat request when call chat GPT endpoint then return chat answer`() {
        //given
        val chatQuestionSample = "src/test/resources/requestChatGPT.json"
        val chatRequest: ChatGPTRequest = JsonConverter.readJsonFile(chatQuestionSample)

        //when
        val chatGPTResponse: ChatGPTResponse = apiClient.askChat(chatRequest)

        //then
        assertChatResponseIsNotBlank(chatGPTResponse)
    }

    private fun assertChatResponseIsNotBlank(chatGPTResponse: ChatGPTResponse) =
        assertThat(chatGPTResponse.choices[0].message.content).isNotBlank
}