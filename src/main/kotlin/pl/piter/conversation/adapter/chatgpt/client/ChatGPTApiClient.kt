package pl.piter.conversation.adapter.chatgpt.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import pl.piter.conversation.adapter.chatgpt.config.FeignConfig
import pl.piter.conversation.adapter.chatgpt.model.ChatGPTRequest
import pl.piter.conversation.adapter.chatgpt.model.ChatGPTResponse

@FeignClient(
    name = "chat-gpt-api-client",
    url = "\${chat-gpt.api.url}",
    configuration = [FeignConfig::class]
)
interface ChatGPTApiClient {

    @PostMapping("/chat/completions")
    fun askChat(@RequestBody request: ChatGPTRequest): ChatGPTResponse
}