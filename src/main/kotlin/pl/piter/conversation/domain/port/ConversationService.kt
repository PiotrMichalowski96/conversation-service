package pl.piter.conversation.domain.port

import pl.piter.conversation.domain.exception.DomainException
import pl.piter.conversation.domain.model.*

class ConversationService(
    private val repository: ConversationRepository,
    private val chatService: ChatService
) {

    operator fun get(conversationId: ConversationId, username: Username): Conversation? = repository.findByIdAndUsername(conversationId, username)

    operator fun get(username: Username): List<Conversation> = repository.findByUsername(username)

    fun delete(conversationId: ConversationId, username: Username) = repository.delete(conversationId, username)

    fun initiateConversation(username: Username, name: ConversationName): Conversation {
        val conversation = Conversation.initiate(username, name)
        return repository.saveOrUpdate(conversation)
    }

    fun updateName(conversationId: ConversationId, username: Username, conversationName: ConversationName): Conversation? {
        val conversation: Conversation = repository.findByIdAndUsername(conversationId, username)
            ?: throw DomainException("Cannot update name with non-existing conversation")
        val updatedConversation: Conversation = conversation.copy(conversationName = conversationName)
        return repository.saveOrUpdate(updatedConversation)
    }

    fun chat(question: Message, conversationId: ConversationId, username: Username): Conversation {
        require(question.messageAuthor == MessageAuthor.USER)

        val conversation: Conversation = repository.findByIdAndUsername(conversationId, username)
            ?: throw DomainException("Cannot chat with non-existing conversation")

        val conversationWithQuestion = addMessageAndPersist(conversation, question)

        val answer: Message = chatService.ask(conversationWithQuestion)
        return addMessageAndPersist(conversationWithQuestion, answer)
    }

    fun removeMessage(messageId: MessageId, conversationId: ConversationId, username: Username): Conversation {
        val conversation: Conversation = repository.findByIdAndUsername(conversationId, username)
            ?: throw DomainException("Cannot remove message from non-existing conversation")

        conversation.removeMessage(messageId)
        return repository.saveOrUpdate(conversation)
    }

    private fun addMessageAndPersist(conversation: Conversation, message: Message): Conversation {
        conversation.addMessage(message)
        return repository.saveOrUpdate(conversation)
    }
}