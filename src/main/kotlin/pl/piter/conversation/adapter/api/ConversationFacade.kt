package pl.piter.conversation.adapter.api

import org.springframework.stereotype.Component
import pl.piter.conversation.domain.port.ConversationService

@Component
class ConversationFacade(private val conversationService: ConversationService) {



}