package com.crm.corecrm.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthenticationFilter,
    private val authenticationProvider: AuthenticationProvider
) {

    companion object {
        private val SWAGGER_URLS = arrayOf(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources/**"
        )
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { }
            .csrf { it.disable() }
            .authorizeHttpRequests { req ->
                req
                    .requestMatchers(*SWAGGER_URLS).permitAll()
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }


    @Bean
    fun corsConfigurationSource(@Value("\${security.cors.allowed_origins:}") origins: List<String>): CorsConfigurationSource {
        println("HELLO " + origins)
        val configuration = CorsConfiguration().apply {
            allowedOrigins = origins
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
