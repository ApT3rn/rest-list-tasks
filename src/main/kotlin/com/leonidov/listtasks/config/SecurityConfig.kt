package com.leonidov.listtasks.config

import com.leonidov.listtasks.config.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@ComponentScan("com.leonidov.listtasks")
@EnableMongoRepositories("com.leonidov.listtasks.persistence")
class SecurityConfig(private val userDetailsService: UserDetailsService,
                     private val jwtAuthFilter: JwtAuthenticationFilter) {

    @Bean
    fun SecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { c -> c.disable() }.cors { c -> c.disable() }

        http.authorizeHttpRequests { c ->
            c.requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/tasks/**")
                .access(WebExpressionAuthorizationManager("isAuthenticated()"))
                .anyRequest().denyAll() }

        http.sessionManagement { c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

        http.authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(12)
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authenticationProvider: DaoAuthenticationProvider = DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}