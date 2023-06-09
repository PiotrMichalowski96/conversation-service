package pl.piter.conversation.domain.port

import pl.piter.conversation.domain.model.Message

interface ChatService {

    fun askChat(question: Message): Message
}