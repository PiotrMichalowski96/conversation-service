package pl.piter.conversation.adapter.chatgpt.model

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

data class ChatGPTRequest(
    @field:NotBlank
    val model: String,
    @field:Valid
    val messages: List<ChatGPTMessage>,
)
