package pl.piter.conversation.adapter.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.piter.conversation.adapter.api.model.*

@RestController
@RequestMapping("conversations")
class ConversationEndpoint(private val conversationFacade: ConversationFacade) {

    @Operation(summary = "Get all conversations")
    @ApiResponse(responseCode = "200", description = "Found conversations", content = [
        Content(mediaType = "application/json", array = ArraySchema(schema =
        Schema(implementation = ConversationResponse::class)))
    ])
    @GetMapping
    fun getConversations(): ResponseEntity<List<ConversationResponse>> =
        ResponseEntity.ok(conversationFacade.getConversations())


    @Operation(summary = "Get conversation by ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found conversation", content = [
            Content(mediaType = "application/json", schema =
            Schema(implementation = ConversationResponse::class))
        ]),
        ApiResponse(responseCode = "404", description = "Did not find conversation", content = [
            Content()
        ])
    ])
    @GetMapping("/{id}")
    fun getConversationById(@PathVariable id: String): ResponseEntity<ConversationResponse> =
        conversationFacade.getConversation(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()


    @Operation(summary = "Create conversation")
    @ApiResponse(responseCode = "200", description = "Created conversation ID", content = [
        Content(mediaType = "application/json", array = ArraySchema(schema =
        Schema(implementation = ConversationIdResponse::class)))
    ])
    @PostMapping
    fun createConversation(@RequestBody conversationRequest: ConversationRequest): ResponseEntity<ConversationIdResponse> =
        ResponseEntity.ok(conversationFacade.createConversation(conversationRequest))


    @Operation(summary = "Update conversation")
    @ApiResponse(responseCode = "204", description = "Conversation is updated", content = [
        Content()
    ])
    @PutMapping("/{id}")
    fun updateConversation(
        @PathVariable id: String,
        @RequestBody conversationRequest: ConversationRequest
    ): ResponseEntity<Any> {
        conversationFacade.updateConversation(id, conversationRequest)
        return ResponseEntity.noContent().build()
    }


    @Operation(summary = "Delete conversation")
    @ApiResponse(responseCode = "204", description = "Conversation is deleted", content = [
        Content()
    ])
    @DeleteMapping("/{id}")
    fun deleteConversation(@PathVariable id: String): ResponseEntity<Any> {
        conversationFacade.deleteConversation(id)
        return ResponseEntity.noContent().build()
    }


    @Operation(summary = "Get all messages with conversation ID")
    @ApiResponse(responseCode = "200", description = "Found messages", content = [
        Content(mediaType = "application/json", array = ArraySchema(schema =
        Schema(implementation = MessageResponse::class)))
    ])
    @GetMapping("/{conversationId}/messages")
    fun getMessages(@PathVariable conversationId: String): ResponseEntity<List<MessageResponse>> =
        ResponseEntity.ok(conversationFacade.getMessages(conversationId))


    @Operation(summary = "Get message by conversation ID and message ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found message", content = [
            Content(mediaType = "application/json", schema =
            Schema(implementation = MessageResponse::class))
        ]),
        ApiResponse(responseCode = "404", description = "Did not find message", content = [
            Content()
        ])
    ])
    @GetMapping("/{conversationId}/messages/{messageId}")
    fun getMessageById(
        @PathVariable conversationId: String,
        @PathVariable messageId: String
    ): ResponseEntity<MessageResponse> =
        conversationFacade.getMessage(conversationId, messageId)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()


    @Operation(summary = "Create message")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Created message ID", content = [
            Content(mediaType = "application/json", array = ArraySchema(schema =
            Schema(implementation = MessageIdResponse::class)))
        ]),
        ApiResponse(responseCode = "404", description = "Did not find message with conversation ID",
            content = [Content()]
        )
    ])
    @PostMapping("/{conversationId}/messages")
    fun createMessage(
        @PathVariable conversationId: String,
        @RequestBody messageRequest: MessageRequest
    ): ResponseEntity<MessageIdResponse> =
        conversationFacade.createMessage(conversationId, messageRequest)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()


    @Operation(summary = "Delete message")
    @ApiResponse(responseCode = "204", description = "Message is deleted", content = [
        Content()
    ])
    @DeleteMapping("/{conversationId}/messages/{messageId}")
    fun deleteMessage(@PathVariable conversationId: String, @PathVariable messageId: String): ResponseEntity<Any> {
        conversationFacade.deleteMessage(conversationId, messageId)
        return ResponseEntity.noContent().build()
    }
}