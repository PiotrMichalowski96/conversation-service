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
    fun `given conversation id and username when get by id and username then return conversation`() {
        //given
        val sampleNo = 1
        val conversation: Conversation = ConversationTestData.getDomain(sampleNo)
        every { repository.findByIdAndUsername(conversation.conversationId, conversation.username) } returns conversation

        //when
        val actualConversation = conversationService[conversation.conversationId, conversation.username]

        //then
        assertThat(actualConversation).isEqualTo(conversation)
    }

    @Test
    fun `given username when get by username then return conversation`() {
        //given
        val sampleNo = 1
        val conversation: Conversation = ConversationTestData.getDomain(sampleNo)
        val conversations = listOf(conversation)
        every { repository.findByUsername(conversation.username) } returns conversations

        //when
        val actualConversations = conversationService[conversation.username]

        //then
        assertThat(actualConversations).hasSameElementsAs(conversations)
    }

    @Test
    fun `given conversation id and username when delete then call delete from repository`() {
        //given
        val conversationId = ConversationId(UUID.randomUUID().toString())
        val username = Username("username")
        every { repository.delete(conversationId, username) } just runs

        //when
        conversationService.delete(conversationId, username)

        //then
        verify { repository.delete(conversationId, username) }
    }

    @Test
    fun `given user id, conversation name and username when initiate then return conversation`() {
        //given
        val username = Username(UUID.randomUUID().toString())
        val name = ConversationName("some name")
        val expectedConversation = Conversation.initiate(username, name)
        every { repository.saveOrUpdate(any()) } returnsArgument 0

        //when
        val actualConversation = conversationService.initiateConversation(username, name)

        //then
        assertThat(actualConversation).usingRecursiveComparison()
            .ignoringFields("conversationId.id", "conversationDateTime.createdAt")
            .isEqualTo(expectedConversation)
    }

    @Test
    fun `given question, conversation id and username when ask chat then return conversation`() {
        //given
        val question = createQuestion()
        val conversation: Conversation = ConversationTestData.getDomain(0)

        every { repository.findByIdAndUsername(conversation.conversationId, conversation.username) } returns conversation
        mockConversationWithQuestion(conversation, question)
        mockConversationWithAnswer(conversation)

        //when
        val actualConversation = conversationService.chat(question, conversation.conversationId, conversation.username)

        //then
        assertThat(actualConversation).isEqualTo(conversation)
    }

    @Test
    fun `given question, conversation id and username when conversation does not exist then throw exception`() {
        //given
        val question = createQuestion()
        val conversation: Conversation = ConversationTestData.getDomain(0)

        every { repository.findByIdAndUsername(conversation.conversationId, conversation.username) } returns null

        //whenThen
        assertThatThrownBy { conversationService.chat(question, conversation.conversationId, conversation.username) }
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
        assertThatThrownBy { conversationService.chat(questionWithChatAuthor, conversation.conversationId, conversation.username) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `given message, conversation id and username when remove message then return conversation`() {
        //given
        val sampleNo = 2
        val conversation: Conversation = ConversationTestData.getDomain(sampleNo)
        val expectedMessagesCount = conversation.conversationMessages.messages.size - 1
        val message: Message = conversation.conversationMessages.messages.last()

        every { repository.findByIdAndUsername(conversation.conversationId, conversation.username) } returns conversation
        every { repository.saveOrUpdate(any()) } returnsArgument 0

        //when
        val actualConversation = conversationService.removeMessage(message.messageId, conversation.conversationId, conversation.username)

        //then
        assertThat(actualConversation.conversationMessages.messages).hasSize(expectedMessagesCount)
    }

    @Test
    fun `given conversation name and username when update name then return conversation with new name`() {
        //given
        val sampleNo = 2
        val conversation: Conversation = ConversationTestData.getDomain(sampleNo)
        val username: Username = conversation.username
        val newName = ConversationName("new-name")

        every { repository.findByIdAndUsername(conversation.conversationId, username) } returns conversation
        every { repository.saveOrUpdate(any()) } returnsArgument 0

        //when
        val updatedConversation: Conversation? = conversationService.updateName(conversation.conversationId, username, newName)

        //then
        assertThat(updatedConversation?.conversationName).isEqualTo(newName)

        assertThat(updatedConversation)
            .usingRecursiveComparison()
            .ignoringFields("conversationName")
            .isEqualTo(conversation)
    }

    @Test
    fun `given conversation does not exist when update name then return null`() {
        //given
        val conversationId = ConversationId.random()
        val username = Username("user1")
        val newName = ConversationName("new-name")

        every { repository.findByIdAndUsername(conversationId, username) } returns null

        //whenThen
        assertThatThrownBy { conversationService.updateName(conversationId, username, newName) }
            .isInstanceOf(DomainException::class.java)
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