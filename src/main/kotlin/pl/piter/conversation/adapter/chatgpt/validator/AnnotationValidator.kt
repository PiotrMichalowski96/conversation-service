package pl.piter.conversation.adapter.chatgpt.validator

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import pl.piter.conversation.adapter.chatgpt.exception.ChatValidationException

@Component
class AnnotationValidator(private val validator: Validator) {

    companion object {
        @JvmStatic
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    fun validate(toValidate: Any) {
        val errors: Errors = BeanPropertyBindingResult(toValidate, toValidate::class.simpleName ?: "any")
        validator.validate(toValidate, errors)
        if (errors.hasErrors()) {
            throw ChatValidationException.of(errors)
        }
    }
}