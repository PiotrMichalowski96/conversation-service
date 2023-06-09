package pl.piter.conversation.adapter.chatgpt.model

import jakarta.validation.constraints.NotBlank

data class ChatGPTMessage(
    val role: Role,
    @field:NotBlank
    val content: String,
)
