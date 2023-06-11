package pl.piter.conversation.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.piter.conversation.domain.port.ChatService
import pl.piter.conversation.domain.port.ConversationRepository
import pl.piter.conversation.domain.port.ConversationService

@Configuration
class BeanConfig {

    @Bean
    fun conversationService(repository: ConversationRepository, chatService: ChatService) =
        ConversationService(repository, chatService)
}