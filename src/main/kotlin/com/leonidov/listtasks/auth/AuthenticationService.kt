package com.leonidov.listtasks.auth

import com.leonidov.listtasks.config.jwt.JwtService
import com.leonidov.listtasks.model.User
import com.leonidov.listtasks.model.enums.Role
import com.leonidov.listtasks.persistence.UserRepository
import com.leonidov.listtasks.pojo.AuthenticationRequest
import com.leonidov.listtasks.pojo.AuthenticationResponse
import com.leonidov.listtasks.pojo.JwtResponse
import com.leonidov.listtasks.pojo.MessageResponse
import org.springframework.context.annotation.Lazy
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService (private val repository: UserRepository,
                             private val jwtService: JwtService,
                             @Lazy private val passwordEncoder: PasswordEncoder,
                             @Lazy private val authenticationManager: AuthenticationManager,
                             @Lazy private val redisTemplate: RedisTemplate<String, String>) : UserDetailsService {

    fun register(request: AuthenticationRequest): Any {
        if (repository.findUserBy_username(request.username).isPresent)
            return MessageResponse("Username: ${request.username} уже зарегистрирован, выберите другой!")
        val user = repository.save(User(request.username, passwordEncoder.encode(request.password), Role.USER))
        val accessToken: String = jwtService.generateAccessToken(user)
        val refreshToken: String = jwtService.generateRefreshToken(user)
        redisTemplate.opsForValue().set(user.username, refreshToken)
        return AuthenticationResponse(accessToken, refreshToken)
    }

    fun authenticate(request: AuthenticationRequest): Any {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.username, request.password
                )
            )
        } catch (e: AuthenticationException) {
            return MessageResponse("Неверный username или password!")
        }
        val user = repository.findUserBy_username(request.username)
            .orElseThrow{UsernameNotFoundException("User with username: ${request.username} not found!")}
        val accessToken: String = jwtService.generateAccessToken(user)
        val refreshToken:String = jwtService.generateRefreshToken(user)
        redisTemplate.opsForValue().set(user.username, refreshToken)
        return AuthenticationResponse(accessToken, refreshToken)
    }

    fun getAccessToken(refreshToken: String): Any {
        if (jwtService.validateRefreshToken(refreshToken)) {
            val username: String = jwtService.extractUsernameFromRefreshToken(refreshToken)
            val getRefreshToken: String? = redisTemplate.opsForValue().get(username)
            return if (getRefreshToken != null && getRefreshToken == refreshToken) {
                val userDetails: UserDetails = loadUserByUsername(username)
                val accessToken: String = jwtService.generateAccessToken(userDetails)
                JwtResponse(accessToken, "")
            } else
                MessageResponse("Неверный токен!")
        }
        return MessageResponse("Невалидный токен!")
    }

    fun refresh(refreshToken: String): Any {
        if (jwtService.validateRefreshToken(refreshToken)) {
            val username: String = jwtService.extractUsernameFromRefreshToken(refreshToken)
            val getRefreshToken: String? = redisTemplate.opsForValue().get(username)
            return if (getRefreshToken != null && getRefreshToken == refreshToken) {
                val userDetails: UserDetails = loadUserByUsername(username)
                val accessToken: String = jwtService.generateAccessToken(userDetails)
                val newRefreshToken: String = jwtService.generateRefreshToken(userDetails)
                redisTemplate.opsForValue().set(username, newRefreshToken)
                JwtResponse(accessToken, newRefreshToken)
            } else
                MessageResponse("Неверный токен!")
        }
        return MessageResponse("Невалидный токен!")
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return repository.findUserBy_username(username).orElseThrow {
            UsernameNotFoundException("User with username: $username not found") }
    }
}