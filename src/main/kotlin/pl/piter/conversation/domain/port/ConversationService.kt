package pl.piter.conversation.domain.port

import pl.piter.conversation.domain.exception.DomainException
import pl.piter.conversation.domain.model.*

class ConversationService(
    private val repository: ConversationRepository,
    private val chatService: ChatService
) {

    fun get(conversationId: ConversationId): Conversation? = repository.findById(conversationId)

    fun get(userId: UserId): List<Conversation> = repository.findByUserId(userId)

    fun delete(conversationId: ConversationId) = repository.delete(conversationId)

    fun initiateConversation(userId: UserId, name: ConversationName): Conversation {
        val conversation = Conversation.initiate(userId, name)
        return repository.saveOrUpdate(conversation)
    }

    fun chat(question: Message, conversationId: ConversationId): Conversation {
        val conversation: Conversation = repository.findById(conversationId)
            ?: throw DomainException("Cannot chat with non-existing conversation")

        addMessageAndPersist(conversation, question)

        val answer: Message = chatService.askChat(question)
        return addMessageAndPersist(conversation, answer)
    }

    fun removeMessage(message: Message, conversationId: ConversationId): Conversation {
        val conversation: Conversation = repository.findById(conversationId)
            ?: throw DomainException("Cannot remove message from non-existing conversation")

        conversation.removeMessage(message)
        return repository.saveOrUpdate(conversation)
    }

    private fun addMessageAndPersist(conversation: Conversation, message: Message): Conversation {
        conversation.addMessage(message)
        return repository.saveOrUpdate(conversation)
    }
}