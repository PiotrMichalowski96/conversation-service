package pl.piter.conversation.adapter.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.jwt.*

@EnableWebSecurity
@Configuration
class SecurityConfig(@Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}") private val issuer: String) {

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return JwtDecoders.fromOidcIssuerLocation(issuer)
    }
}