package pl.piter.conversation.adapter.api

import org.springframework.stereotype.Component
import pl.piter.conversation.adapter.api.mapper.toDomain
import pl.piter.conversation.adapter.api.mapper.toResponse
import pl.piter.conversation.adapter.api.model.*
import pl.piter.conversation.domain.model.*
import pl.piter.conversation.domain.port.ConversationService
import java.util.*

@Component
class ConversationFacade(private val conversationService: ConversationService) {

    fun getConversations(): List<ConversationResponse> {
        val userId = UserId(UUID.randomUUID().toString()) //TODO: temporary random userId
        return conversationService[userId].map { it.toResponse() }
    }

    fun getConversation(id: String): ConversationResponse? {
        val conversationId = ConversationId(id)
        return conversationService[conversationId]?.toResponse()
    }

    fun createConversation(request: ConversationRequest): ConversationIdResponse {
        val userId = UserId(UUID.randomUUID().toString()) //TODO: temporary random userId
        val conversationName = ConversationName(request.name)
        val conversation = conversationService.initiateConversation(userId, conversationName)
        return ConversationIdResponse(conversation.conversationId.id)
    }

    fun updateConversation(id: String, request: ConversationRequest) {
        val conversationId = ConversationId(id)
        val conversationName = ConversationName(request.name)
        conversationService.updateName(conversationId, conversationName)
    }

    fun deleteConversation(id: String) {
        val conversationId = ConversationId(id)
        conversationService.delete(conversationId)
    }

    fun getMessages(conversationId: String): List<MessageResponse> {
        val convId = ConversationId(conversationId)
        return conversationService[convId]?.conversationMessages?.messages
            ?.map { it.toResponse() }
            ?: emptyList()
    }

    fun getMessage(conversationId: String, messageId: String): MessageResponse? {
        val convId = ConversationId(conversationId)
        return conversationService[convId]?.conversationMessages?.messages
            ?.map { it.toResponse() }
            ?.firstOrNull { messageResponse -> messageResponse.id == messageId }
    }

    fun createMessage(conversationId: String, messageRequest: MessageRequest): MessageIdResponse? {
        val convId = ConversationId(conversationId)
        val question: Message = messageRequest.toDomain()
        conversationService.chat(question, convId)
        return MessageIdResponse(question.messageId.id)
    }

    fun deleteMessage(conversationId: String, messageId: String) {
        val convId = ConversationId(conversationId)
        val msgId = MessageId(messageId)
        conversationService.removeMessage(msgId, convId)
    }
}