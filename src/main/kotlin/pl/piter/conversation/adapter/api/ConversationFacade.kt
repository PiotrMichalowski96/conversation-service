package pl.piter.conversation.adapter.api

import org.springframework.stereotype.Component
import pl.piter.conversation.adapter.api.mapper.toDomain
import pl.piter.conversation.adapter.api.mapper.toResponse
import pl.piter.conversation.adapter.api.model.*
import pl.piter.conversation.adapter.api.util.SecurityContextHelper
import pl.piter.conversation.domain.model.*
import pl.piter.conversation.domain.port.ConversationService

@Component
class ConversationFacade(private val conversationService: ConversationService,
                         private val securityContextHelper: SecurityContextHelper) {

    fun getConversations(): List<ConversationResponse> {
        val username = securityContextHelper.fetchUsername()
        return conversationService[username].map { it.toResponse() }
    }

    fun getConversation(id: String): ConversationResponse? {
        val username = securityContextHelper.fetchUsername()
        val conversationId = ConversationId(id)
        return conversationService[conversationId, username]?.toResponse()
    }

    fun createConversation(request: ConversationRequest): ConversationIdResponse {
        val username = securityContextHelper.fetchUsername()
        val conversationName = ConversationName(request.name)
        val conversation = conversationService.initiateConversation(username, conversationName)
        return ConversationIdResponse(conversation.conversationId.id)
    }

    fun updateConversation(id: String, request: ConversationRequest) {
        val username = securityContextHelper.fetchUsername()
        val conversationId = ConversationId(id)
        val conversationName = ConversationName(request.name)
        conversationService.updateName(conversationId, username, conversationName)
    }

    fun deleteConversation(id: String) {
        val username = securityContextHelper.fetchUsername()
        val conversationId = ConversationId(id)
        conversationService.delete(conversationId, username)
    }

    fun getMessages(conversationId: String): List<MessageResponse> {
        val username = securityContextHelper.fetchUsername()
        val convId = ConversationId(conversationId)
        return conversationService[convId, username]?.conversationMessages?.messages
            ?.map { it.toResponse() }
            ?: emptyList()
    }

    fun getMessage(conversationId: String, messageId: String): MessageResponse? {
        val username = securityContextHelper.fetchUsername()
        val convId = ConversationId(conversationId)
        return conversationService[convId, username]?.conversationMessages?.messages
            ?.map { it.toResponse() }
            ?.firstOrNull { messageResponse -> messageResponse.id == messageId }
    }

    fun createMessage(conversationId: String, messageRequest: MessageRequest): MessageIdResponse? {
        val username = securityContextHelper.fetchUsername()
        val convId = ConversationId(conversationId)
        val question: Message = messageRequest.toDomain()
        conversationService.chat(question, convId, username)
        return MessageIdResponse(question.messageId.id)
    }

    fun deleteMessage(conversationId: String, messageId: String) {
        val username = securityContextHelper.fetchUsername()
        val convId = ConversationId(conversationId)
        val msgId = MessageId(messageId)
        conversationService.removeMessage(msgId, convId, username)
    }
}