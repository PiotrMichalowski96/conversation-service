package pl.piter.conversation.adapter.api

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.piter.conversation.adapter.api.model.*

@RestController
@RequestMapping("conversations")
class ConversationEndpoint(conversationFacade: ConversationFacade) {

    @GetMapping
    fun getConversations(): List<ConversationResponse> {
        TODO("implement")
    }

    @GetMapping("/{id}")
    fun getConversationById(@PathVariable id: String): List<ConversationResponse> {
        TODO("implement")
    }

    @PostMapping
    fun createConversation(@RequestBody conversationRequest: ConversationRequest): ConversationIdResponse {
        TODO("implement")
    }

    @PutMapping("/{id}")
    fun updateConversation(
        @PathVariable id: String,
        @RequestBody conversationRequest: ConversationRequest
    ) {
        TODO("implement")
    }

    @DeleteMapping("/{id}")
    fun deleteConversation(@PathVariable id: String) {
        TODO("implement")
    }

    @GetMapping("/{conversationId}/messages")
    fun getMessages(@PathVariable conversationId: String): List<MessageResponse> {
        TODO("implement")
    }

    @GetMapping("/{conversationId}/messages/{messageId}")
    fun getMessageById(
        @PathVariable conversationId: String,
        @PathVariable messageId: String
    ): MessageResponse {
        TODO("implement")
    }

    @PostMapping("/{conversationId}/messages")
    fun createMessage(
        @PathVariable conversationId: String,
        @RequestBody messageRequest: MessageRequest
    ): MessageIdResponse {
        TODO("implement")
    }

    @DeleteMapping("/{conversationId}/messages/{messageId}")
    fun deleteMessage(@PathVariable conversationId: String, @PathVariable messageId: String) {
        TODO("implement")
    }
}