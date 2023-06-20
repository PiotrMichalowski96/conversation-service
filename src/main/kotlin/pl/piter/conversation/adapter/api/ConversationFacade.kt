package pl.piter.conversation.adapter.api

import org.springframework.stereotype.Component
import pl.piter.conversation.adapter.api.mapper.toDomain
import pl.piter.conversation.adapter.api.mapper.toResponse
import pl.piter.conversation.adapter.api.model.*
import pl.piter.conversation.domain.model.*
import pl.piter.conversation.domain.port.ConversationService

@Component
class ConversationFacade(private val conversationService: ConversationService) {

    fun getConversations(): List<ConversationResponse> {
        val username = Username("username") //TODO: temporary hardcoded
        return conversationService[username].map { it.toResponse() }
    }

    fun getConversation(id: String): ConversationResponse? {
        val conversationId = ConversationId(id)
        val username = Username("username") //TODO: temporary hardcoded
        return conversationService[conversationId, username]?.toResponse()
    }

    fun createConversation(request: ConversationRequest): ConversationIdResponse {
        val username = Username("username") //TODO: temporary hardcoded
        val conversationName = ConversationName(request.name)
        val conversation = conversationService.initiateConversation(username, conversationName)
        return ConversationIdResponse(conversation.conversationId.id)
    }

    fun updateConversation(id: String, request: ConversationRequest) {
        val conversationId = ConversationId(id)
        val conversationName = ConversationName(request.name)
        val username = Username("username") //TODO: temporary hardcoded
        conversationService.updateName(conversationId, username, conversationName)
    }

    fun deleteConversation(id: String) {
        val conversationId = ConversationId(id)
        val username = Username("username") //TODO: temporary hardcoded
        conversationService.delete(conversationId, username)
    }

    fun getMessages(conversationId: String): List<MessageResponse> {
        val convId = ConversationId(conversationId)
        val username = Username("username") //TODO: temporary hardcoded
        return conversationService[convId, username]?.conversationMessages?.messages
            ?.map { it.toResponse() }
            ?: emptyList()
    }

    fun getMessage(conversationId: String, messageId: String): MessageResponse? {
        val convId = ConversationId(conversationId)
        val username = Username("username") //TODO: temporary hardcoded
        return conversationService[convId, username]?.conversationMessages?.messages
            ?.map { it.toResponse() }
            ?.firstOrNull { messageResponse -> messageResponse.id == messageId }
    }

    fun createMessage(conversationId: String, messageRequest: MessageRequest): MessageIdResponse? {
        val convId = ConversationId(conversationId)
        val username = Username("username") //TODO: temporary hardcoded
        val question: Message = messageRequest.toDomain()
        conversationService.chat(question, convId, username)
        return MessageIdResponse(question.messageId.id)
    }

    fun deleteMessage(conversationId: String, messageId: String) {
        val convId = ConversationId(conversationId)
        val username = Username("username") //TODO: temporary hardcoded
        val msgId = MessageId(messageId)
        conversationService.removeMessage(msgId, convId, username)
    }
}