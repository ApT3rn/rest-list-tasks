package com.leonidov.listtasks.auth

import com.leonidov.listtasks.config.jwt.JwtService
import com.leonidov.listtasks.model.User
import com.leonidov.listtasks.model.enums.Role
import com.leonidov.listtasks.persistence.UserRepository
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService (private val repository: UserRepository,
                             private val jwtService: JwtService,
                             @Lazy private val passwordEncoder: PasswordEncoder,
                             @Lazy private val authenticationManager: AuthenticationManager) : UserDetailsService {

    fun register(request: AuthenticationRequest): AuthenticationResponse {
        if (repository.findUserBy_username(request.username).isPresent)
            return AuthenticationResponse("Username: ${request.username} уже зарегистрирован, выберите другой!")
        val user: User = User(request.username, passwordEncoder.encode(request.password), Role.USER)
        repository.save(user)
        val jwtToken: String = jwtService.generateToken(user)
        return AuthenticationResponse(jwtToken)
    }

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.username, request.password
            )
        )
        val user = repository.findUserBy_username(request.username)
            .orElseThrow{UsernameNotFoundException("User with username: ${request.username} not found!")}
        val jwtToken: String = jwtService.generateToken(user)
        return AuthenticationResponse(jwtToken)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return repository.findUserBy_username(username).orElseThrow {
            UsernameNotFoundException("User with username: $username not found") }
    }
}