package pl.piter.conversation.adapter.api

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import pl.piter.conversation.domain.model.Conversation
import pl.piter.conversation.domain.model.ConversationId
import pl.piter.conversation.domain.model.UserId
import pl.piter.conversation.domain.port.ConversationService
import pl.piter.conversation.util.ConversationTestData
import java.util.*

@ExtendWith(MockKExtension::class)
@WebMvcTest(ConversationEndpoint::class)
@Import(ConversationFacade::class)
class ConversationEndpointTest(@Autowired private val mvc: MockMvc) {

    @MockkBean
    private lateinit var conversationService: ConversationService

    @Test
    fun `given conversations endpoint when get call then return conversation response list`() {
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
    fun `given conversation id when call get by id endpoint then return conversation response`() {
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
    fun `given non-existing conversation id when call get by id endpoint then return 404`() {
        //given
        val id: String = UUID.randomUUID().toString()
        val conversationId = ConversationId(id)
        every { conversationService[conversationId] } returns null

        //whenThen
        mvc.perform(get("/conversations/${id}"))
            .andExpect(status().isNotFound)
    }


}