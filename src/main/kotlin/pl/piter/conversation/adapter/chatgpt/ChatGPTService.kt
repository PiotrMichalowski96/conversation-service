package pl.piter.conversation.adapter.chatgpt

import org.springframework.stereotype.Service
import pl.piter.conversation.adapter.chatgpt.client.ChatGPTApiClient
import pl.piter.conversation.adapter.chatgpt.mapper.toChatRequest
import pl.piter.conversation.adapter.chatgpt.mapper.toDomain
import pl.piter.conversation.adapter.chatgpt.model.ChatGPTRequest
import pl.piter.conversation.adapter.chatgpt.model.ChatGPTResponse
import pl.piter.conversation.adapter.chatgpt.validator.AnnotationValidator
import pl.piter.conversation.domain.model.Conversation
import pl.piter.conversation.domain.model.Message
import pl.piter.conversation.domain.port.ChatService

@Service
class ChatGPTService(
    private val chatApiClient: ChatGPTApiClient,
    private val validator: AnnotationValidator
) : ChatService() {

    override fun askChat(conversation: Conversation): Message {
        val request: ChatGPTRequest = performAndValidate { conversation.toChatRequest() }
        val response: ChatGPTResponse = performAndValidate { chatApiClient.askChat(request) }
        return response.toDomain()
    }

    private fun <T: Any> performAndValidate(supplyAction: () -> T): T {
        val result: T = supplyAction.invoke()
        validator.validate(result)
        return result
    }
}