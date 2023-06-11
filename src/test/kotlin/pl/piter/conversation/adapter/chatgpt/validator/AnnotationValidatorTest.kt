package pl.piter.conversation.adapter.chatgpt.validator

import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.validation.Validator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import pl.piter.conversation.adapter.chatgpt.exception.ChatValidationException
import pl.piter.conversation.adapter.chatgpt.model.ChatGPTRequest
import pl.piter.conversation.util.JsonConverter

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [LocalValidatorFactoryBean::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AnnotationValidatorTest {

    @Autowired
    private lateinit var validator: Validator

    private lateinit var annotationValidator: AnnotationValidator

    @BeforeAll
    fun init() {
        annotationValidator = AnnotationValidator(validator)
    }

    @Test
    fun `given invalid request when validate then throw exception`() {
        //given
        val invalidRequest = ChatGPTRequest("", listOf())

        //whenThen
        assertThatThrownBy { annotationValidator.validate(invalidRequest) }
            .isInstanceOf(ChatValidationException::class.java)
    }

    @Test
    fun `given valid request when validate then do not throw exception`() {
        //given
        val requestSample = "src/test/resources/requestChatGPT.json"
        val request: ChatGPTRequest = JsonConverter.readJsonFile(requestSample)

        //whenThen
        assertThatCode { annotationValidator.validate(request) }
            .doesNotThrowAnyException()
    }
}