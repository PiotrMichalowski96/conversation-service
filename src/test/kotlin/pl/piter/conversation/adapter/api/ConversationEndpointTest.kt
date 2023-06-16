package pl.piter.conversation.adapter.api

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import pl.piter.conversation.adapter.api.model.ConversationRequest
import pl.piter.conversation.adapter.api.model.MessageRequest
import pl.piter.conversation.domain.exception.DomainException
import pl.piter.conversation.domain.model.*
import pl.piter.conversation.domain.port.ConversationService
import pl.piter.conversation.util.ConversationTestData
import pl.piter.conversation.util.JsonConverter.asJsonString
import java.util.*

@ExtendWith(MockKExtension::class)
@WebMvcTest(ConversationEndpoint::class)
@Import(ConversationFacade::class)
class ConversationEndpointTest(@Autowired private val mvc: MockMvc) {

    @MockkBean
    private lateinit var conversationService: ConversationService

    @Test
    fun `given conversations endpoint when call get endpoint then return conversation response list`() {
        //given
        val conversations: List<Conversation> = ConversationTestData.getDomains()
        every { conversationService[any<UserId>()] } returns conversations

        //whenThen
        mvc.perform(get("/conversations"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
    }

    @Test
    fun `given conversation id when call by id get endpoint then return conversation response`() {
        //given
        val conversation: Conversation = ConversationTestData.getDomain(0)
        val id: String = conversation.conversationId.id
        every { conversationService[conversation.conversationId] } returns conversation

        //whenThen
        mvc.perform(get("/conversations/${id}"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(id))
    }

    @Test
    fun `given non-existing conversation id when call by id get endpoint then return 404`() {
        //given
        val id: String = UUID.randomUUID().toString()
        val conversationId = ConversationId(id)
        every { conversationService[conversationId] } returns null

        //whenThen
        mvc.perform(get("/conversations/${id}"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `given conversation request when call create conversation post endpoint then id response`() {
        //given
        val conversationRequest = ConversationRequest("conv-name")
        val conversationName = ConversationName(conversationRequest.name)
        val conversation = ConversationTestData.getDomain(0)
        every { conversationService.initiateConversation(any<UserId>(), conversationName) } returns conversation

        //whenThen
        mvc.perform(post("/conversations")
            .content(asJsonString(conversationRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(conversation.conversationId.id))
    }

    @Test
    fun `given id and conversation request when call update conversation put endpoint then return 204`() {
        //given
        val id = UUID.randomUUID().toString()
        val conversationRequest = ConversationRequest("conv-name")
        val conversationId = ConversationId(id)
        val conversationName = ConversationName(conversationRequest.name)
        val conversation: Conversation = ConversationTestData.getDomain(0)
        every { conversationService.updateName(conversationId, conversationName) } returns conversation

        //whenThen
        mvc.perform(put("/conversations/${id}")
            .content(asJsonString(conversationRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
    }

    @Disabled
    @Test
    fun `given non-existing id when call update conversation put endpoint then return 422`() {
        //given
        val nonExistingId = UUID.randomUUID().toString()
        val conversationRequest = ConversationRequest("conv-name")
        val conversationId = ConversationId(nonExistingId)
        val conversationName = ConversationName(conversationRequest.name)
        every { conversationService.updateName(conversationId, conversationName) } returns null

        //whenThen
        mvc.perform(put("/conversations/${nonExistingId}")
            .content(asJsonString(conversationRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `given conversation id when call delete by id endpoint then return 204`() {
        //given
        val id = UUID.randomUUID().toString()
        val conversationId = ConversationId(id)
        justRun { conversationService.delete(conversationId) }

        //whenThen
        mvc.perform(delete("/conversations/${id}"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `given conversation id when call find messages then return messages response`() {
        //given
        val id = UUID.randomUUID().toString()
        val conversationId = ConversationId(id)
        val conversation: Conversation = ConversationTestData.getDomain(0)
        every { conversationService[conversationId] } returns conversation

        //whenThen
        mvc.perform(get("/conversations/${id}/messages"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
    }

    @Test
    fun `given non-existing conversation id when call find messages then return empty message list`() {
        //given
        val nonExistingId = UUID.randomUUID().toString()
        val nonExistingConversationId = ConversationId(nonExistingId)
        every { conversationService[nonExistingConversationId] } returns null

        //whenThen
        mvc.perform(get("/conversations/${nonExistingId}/messages"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `given conversation id and message id when call find message by id then return message response`() {
        //given
        val id = UUID.randomUUID().toString()
        val conversationId = ConversationId(id)
        val conversation: Conversation = ConversationTestData.getDomain(0)
        val messageId: String = conversation.conversationMessages.messages[0].messageId.id
        every { conversationService[conversationId] } returns conversation

        //whenThen
        mvc.perform(get("/conversations/${id}/messages/${messageId}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(messageId))
    }

    @Test
    fun `given non-existing conversation id and message id when call find message by id then return 404`() {
        //given
        val nonExistingId = UUID.randomUUID().toString()
        val nonExistingConversationId = ConversationId(nonExistingId)
        val messageId = UUID.randomUUID().toString()
        every { conversationService[nonExistingConversationId] } returns null

        //whenThen
        mvc.perform(get("/conversations/${nonExistingId}/messages/${messageId}"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `given conversation id and non-existing message id when call find message by id then return 404`() {
        //given
        val id = UUID.randomUUID().toString()
        val conversationId = ConversationId(id)
        val conversation: Conversation = ConversationTestData.getDomain(0)
        val nonExistingMessageId = UUID.randomUUID().toString()
        every { conversationService[conversationId] } returns conversation

        //whenThen
        mvc.perform(get("/conversations/${id}/messages/${nonExistingMessageId}"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `given conversation id and message request when call create message then return message id response`() {
        //given
        val id = UUID.randomUUID().toString()
        val conversationId = ConversationId(id)
        val conversation: Conversation = ConversationTestData.getDomain(0)
        val messageRequest = MessageRequest("What is a Riemann hypothesis?")
        every { conversationService.chat(any<Message>(), conversationId) } returns conversation

        //whenThen
        mvc.perform(post("/conversations/${id}/messages")
            .content(asJsonString(messageRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
    }

    @Disabled
    @Test
    fun `given non-existing conversation id and message request when call create message then return 422`() {
        //given
        val nonExistingId = UUID.randomUUID().toString()
        val nonExistingConversationId = ConversationId(nonExistingId)
        val messageRequest = MessageRequest("What is a Riemann hypothesis?")
        every {
            conversationService.chat(any<Message>(), nonExistingConversationId)
        } throws DomainException("Cannot chat with non-existing conversation")

        //whenThen
        mvc.perform(post("/conversations/${nonExistingId}/messages")
            .content(asJsonString(messageRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `given conversation id and message id when call delete message then return 204`() {
        //given
        val conversationId = UUID.randomUUID().toString()
        val messageId = UUID.randomUUID().toString()
        val conversation: Conversation = ConversationTestData.getDomain(0)
        every {
            conversationService.removeMessage(MessageId(messageId), ConversationId(conversationId))
        } returns conversation

        //whenThen
        mvc.perform(delete("/conversations/${conversationId}/messages/${messageId}"))
            .andExpect(status().isNoContent)
    }

    @Disabled
    @Test
    fun `given non-existing conversation id and message id when call delete message then return 422`() {
        //given
        val nonExistingConversationId = UUID.randomUUID().toString()
        val messageId = UUID.randomUUID().toString()
        every {
            conversationService.removeMessage(MessageId(messageId), ConversationId(nonExistingConversationId))
        } throws DomainException("Cannot remove message from non-existing conversation")

        //whenThen
        mvc.perform(delete("/conversations/${nonExistingConversationId}/messages/${messageId}"))
            .andExpect(status().isUnprocessableEntity)
    }
}