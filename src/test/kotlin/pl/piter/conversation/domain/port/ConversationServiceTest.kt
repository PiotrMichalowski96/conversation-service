package pl.piter.conversation.domain.port

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.piter.conversation.domain.exception.DomainException
import pl.piter.conversation.domain.model.*
import pl.piter.conversation.util.ConversationTestData
import java.util.*

@ExtendWith(MockKExtension::class)
class ConversationServiceTest {

    @InjectMockKs
    private lateinit var conversationService: ConversationService

    @MockK
    private lateinit var repository: ConversationRepository

    @MockK
    private lateinit var chatService: ChatService

    @Test
    fun `given conversation id when get by id then return conversation`() {
        //given
        val sampleNo = 1
        val conversation: Conversation = ConversationTestData.getDomain(sampleNo)
        every { repository.findById(conversation.conversationId) } returns conversation

        //when
        val actualConversation = conversationService[conversation.conversationId]

        //then
        assertThat(actualConversation).isEqualTo(conversation)
    }

    @Test
    fun `given user id when get by user id then return conversation`() {
        //given
        val sampleNo = 1
        val conversation: Conversation = ConversationTestData.getDomain(sampleNo)
        val conversations = listOf(conversation)
        every { repository.findByUserId(conversation.userId) } returns conversations

        //when
        val actualConversations = conversationService[conversation.userId]

        //then
        assertThat(actualConversations).hasSameElementsAs(conversations)
    }

    @Test
    fun `given conversation id when delete then call delete from repository`() {
        //given
        val conversationId = ConversationId(UUID.randomUUID().toString())
        every { repository.delete(conversationId) } just runs

        //when
        conversationService.delete(conversationId)

        //then
        verify { repository.delete(conversationId) }
    }

    @Test
    fun `given user id and conversation name when initiate then return conversation`() {
        //given
        val userId = UserId(UUID.randomUUID().toString())
        val name = ConversationName("some name")
        val expectedConversation = Conversation.initiate(userId, name)
        every { repository.saveOrUpdate(any()) } returnsArgument 0

        //when
        val actualConversation = conversationService.initiateConversation(userId, name)

        //then
        assertThat(actualConversation).usingRecursiveComparison()
            .ignoringFields("conversationId.id", "conversationDateTime.createdAt")
            .isEqualTo(expectedConversation)
    }

    @Test
    fun `given question and conversation id when ask chat then return conversation`() {
        //given
        val question = createQuestion()
        val conversation: Conversation = ConversationTestData.getDomain(0)

        every { repository.findById(conversation.conversationId) } returns conversation
        mockConversationWithQuestion(conversation, question)
        mockConversationWithAnswer(conversation)

        //when
        val actualConversation = conversationService.chat(question, conversation.conversationId)

        //then
        assertThat(actualConversation).isEqualTo(conversation)
    }

    @Test
    fun `given question and id when conversation does not exist then throw exception`() {
        //given
        val question = createQuestion()
        val conversation: Conversation = ConversationTestData.getDomain(0)

        every { repository.findById(conversation.conversationId) } returns null

        //whenThen
        assertThatThrownBy { conversationService.chat(question, conversation.conversationId) }
            .isInstanceOf(DomainException::class.java)
    }

    @Test
    fun `given question with chat gpt author when ask chat then throw exception`() {
        //given
        val questionWithChatAuthor = Message(
            MessageId(UUID.randomUUID().toString()),
            MessageAuthor.CHAT_GPT,
            MessageContent("Some content")
        )
        val conversation: Conversation = ConversationTestData.getDomain(0)

        //whenThen
        assertThatThrownBy { conversationService.chat(questionWithChatAuthor, conversation.conversationId) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `given message and id when remove message then return conversation`() {
        //given
        val sampleNo = 2
        val conversation: Conversation = ConversationTestData.getDomain(sampleNo)
        val expectedMessagesCount = conversation.conversationMessages.messages.size - 1
        val message: Message = conversation.conversationMessages.messages.last()

        every { repository.findById(conversation.conversationId) } returns conversation
        every { repository.saveOrUpdate(any()) } returnsArgument 0

        //when
        val actualConversation = conversationService.removeMessage(message, conversation.conversationId)

        //then
        assertThat(actualConversation.conversationMessages.messages).hasSize(expectedMessagesCount)
    }

    private fun createQuestion() = Message(
        MessageId(UUID.randomUUID().toString()),
        MessageAuthor.USER,
        MessageContent("Some question?")
    )

    private fun mockConversationWithQuestion(conversation: Conversation, question: Message) {
        conversation.addMessage(question)
        every { repository.saveOrUpdate(any()) } returnsArgument 0
    }

    private fun mockConversationWithAnswer(conversation: Conversation) {
        val answer = Message(
            MessageId(UUID.randomUUID().toString()),
            MessageAuthor.CHAT_GPT,
            MessageContent("Some answer")
        )
        every { chatService.ask(conversation) } returns answer
        conversation.addMessage(answer)
        every { repository.saveOrUpdate(any()) } returnsArgument 0
    }
}