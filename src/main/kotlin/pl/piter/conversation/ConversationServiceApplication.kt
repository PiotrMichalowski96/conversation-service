package pl.piter.conversation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConversationServiceApplication

fun main(args: Array<String>) {
    runApplication<ConversationServiceApplication>(*args)
}
