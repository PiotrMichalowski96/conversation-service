package pl.piter.conversation.domain.port

import pl.piter.conversation.domain.model.ConversationId
import pl.piter.conversation.domain.model.ConversationName
import pl.piter.conversation.domain.model.Message
import pl.piter.conversation.domain.model.UserId

interface ConversationService {

    fun initiateConversation(userId: UserId, name: ConversationName): ConversationId

    fun addMessage(message: Message)

    fun removeMessage(message: Message)
}