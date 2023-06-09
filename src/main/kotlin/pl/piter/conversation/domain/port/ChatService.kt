package pl.piter.conversation.domain.port

import pl.piter.conversation.domain.model.Conversation
import pl.piter.conversation.domain.model.Message
import pl.piter.conversation.domain.model.MessageAuthor

abstract class ChatService {

    fun ask(conversation: Conversation): Message {
        require(conversation.conversationMessages.messages.last().messageAuthor == MessageAuthor.USER)
        return askChat(conversation)
    }

    protected abstract fun askChat(conversation: Conversation): Message
}