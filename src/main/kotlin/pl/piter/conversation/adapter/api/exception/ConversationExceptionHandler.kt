package pl.piter.conversation.adapter.api.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import pl.piter.conversation.domain.exception.DomainException

@ControllerAdvice
class ConversationExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(DomainException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handle(ex: RuntimeException, request: WebRequest): ResponseEntity<Any>? {
        val responseBody = "Invalid or non-existing conversation ID"
        return handleExceptionInternal(ex, responseBody, HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request)
    }
}