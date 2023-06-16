package pl.piter.conversation.adapter.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.piter.conversation.adapter.api.model.*

@RestController
@RequestMapping("conversations")
class ConversationEndpoint(private val conversationFacade: ConversationFacade) {

    @GetMapping
    fun getConversations(): ResponseEntity<List<ConversationResponse>> =
        ResponseEntity.ok(conversationFacade.getConversations())

    @GetMapping("/{id}")
    fun getConversationById(@PathVariable id: String): ResponseEntity<ConversationResponse> =
        conversationFacade.getConversation(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @PostMapping
    fun createConversation(@RequestBody conversationRequest: ConversationRequest): ResponseEntity<ConversationIdResponse> =
        ResponseEntity.ok(conversationFacade.createConversation(conversationRequest))

    @PutMapping("/{id}")
    fun updateConversation(
        @PathVariable id: String,
        @RequestBody conversationRequest: ConversationRequest
    ): ResponseEntity<Any> {
        conversationFacade.updateConversation(id, conversationRequest)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun deleteConversation(@PathVariable id: String): ResponseEntity<Any> {
        conversationFacade.deleteConversation(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{conversationId}/messages")
    fun getMessages(@PathVariable conversationId: String): ResponseEntity<List<MessageResponse>> =
        ResponseEntity.ok(conversationFacade.getMessages(conversationId))

    @GetMapping("/{conversationId}/messages/{messageId}")
    fun getMessageById(
        @PathVariable conversationId: String,
        @PathVariable messageId: String
    ): ResponseEntity<MessageResponse> =
        conversationFacade.getMessage(conversationId, messageId)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @PostMapping("/{conversationId}/messages")
    fun createMessage(
        @PathVariable conversationId: String,
        @RequestBody messageRequest: MessageRequest
    ): ResponseEntity<MessageIdResponse> =
        conversationFacade.createMessage(conversationId, messageRequest)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{conversationId}/messages/{messageId}")
    fun deleteMessage(@PathVariable conversationId: String, @PathVariable messageId: String): ResponseEntity<Any> {
        conversationFacade.deleteMessage(conversationId, messageId)
        return ResponseEntity.noContent().build()
    }
}