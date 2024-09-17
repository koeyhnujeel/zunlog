package zunza.zunlog.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import zunza.zunlog.config.handler.Http401Handler
import zunza.zunlog.jwt.JwtExceptionFilter
import zunza.zunlog.jwt.JwtRequestFilter
import zunza.zunlog.jwt.JwtUtil
import zunza.zunlog.jwt.LoginFilter

@Configuration
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val objectMapper: ObjectMapper,
    private val jwtUtil: JwtUtil
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtRequestFilter: JwtRequestFilter): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterAt(LoginFilter(
                authenticationManager = authenticationConfiguration.authenticationManager,
                objectMapper = objectMapper,
                jwtUtil = jwtUtil),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .addFilterAfter(jwtRequestFilter, LoginFilter::class.java)
            .addFilterBefore(JwtExceptionFilter(objectMapper), jwtRequestFilter::class.java)
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(HttpMethod.POST, "/posts").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/posts").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/posts").authenticated()
                    .requestMatchers("/subscriptions/**").authenticated()
                    .requestMatchers("/notifications/**").authenticated()
                    .anyRequest().permitAll()
            }
            .exceptionHandling { it.authenticationEntryPoint(Http401Handler(objectMapper)) }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}