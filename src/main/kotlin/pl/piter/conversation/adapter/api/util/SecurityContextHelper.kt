package pl.piter.conversation.adapter.api.util

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import pl.piter.conversation.domain.model.Username

@Component
class SecurityContextHelper {

    companion object {
        const val USERNAME_KEY = "username"
    }

    fun fetchUsername(): Username {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val principal: Any = authentication.principal
        if (principal is Jwt) {
            val username: String = fetchUsernameFromClaims(principal)
            return Username(username)
        }
        throw RuntimeException("Cannot fetch username")
    }

    private fun fetchUsernameFromClaims(principal: Jwt): String {
        val username: Any? = principal.claims[USERNAME_KEY]
        if (username is String) return username
        throw RuntimeException("Cannot fetch username")
    }
}