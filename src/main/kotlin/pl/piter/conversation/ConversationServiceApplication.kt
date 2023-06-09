package pl.piter.conversation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients(basePackages = ["pl.piter.conversation.adapter.chatgpt.client"])
@SpringBootApplication
class ConversationServiceApplication

fun main(args: Array<String>) {
    runApplication<ConversationServiceApplication>(*args)
}
