package com.leonidov.listtasks.service.impl

import com.leonidov.listtasks.config.jwt.JwtService
import com.leonidov.listtasks.model.User
import com.leonidov.listtasks.model.enums.Role
import com.leonidov.listtasks.persistence.TokenRepository
import com.leonidov.listtasks.persistence.UserRepository
import com.leonidov.listtasks.pojo.*
import com.leonidov.listtasks.service.AuthenticationService
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl (private val userRepository: UserRepository,
                                 private val jwtService: JwtService,
                                 private val tokenRepository: TokenRepository,
                                 @Lazy private val passwordEncoder: PasswordEncoder,
                                 @Lazy private val authenticationManager: AuthenticationManager) :
    AuthenticationService, UserDetailsService {

    override fun register(request: AuthenticationRequest): Any {
        if (userRepository.findUserByUsername(request.username).isPresent)
            return MessageResponse("Username: ${request.username} уже зарегистрирован, выберите другой!")
        val user = userRepository.save(User(request.username, passwordEncoder.encode(request.password), Role.USER))
        val accessToken: String = jwtService.generateAccessToken(user)
        val refreshToken: String = jwtService.generateRefreshToken(user)
        tokenRepository.saveTokenAndUsername(user.username, refreshToken)
        return AuthenticationResponse(accessToken, refreshToken)
    }

    override fun authenticate(request: AuthenticationRequest): Any {
        val userDetails: UserDetails
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.username, request.password))
            userDetails = userRepository.findUserByUsername(request.username)
                .orElseThrow{UsernameNotFoundException("Пользователь с ${request.username} не найден!")}
        } catch (e: AuthenticationException) {
            return MessageResponse("Неверный username или password!")
        }
        val accessToken: String = jwtService.generateAccessToken(userDetails)
        val refreshToken:String = jwtService.generateRefreshToken(userDetails)
        tokenRepository.saveTokenAndUsername(userDetails.username, refreshToken)
        return AuthenticationResponse(accessToken, refreshToken)
    }

    override fun getAccessToken(request: JwtRequest): Any {
        if (jwtService.validateRefreshToken(request.refreshToken)) {
            val username: String = jwtService.extractUsernameFromRefreshToken(request.refreshToken)
            val getRefreshToken: String? = tokenRepository.getTokenByUsername(username)
            return if (getRefreshToken != null && getRefreshToken == request.refreshToken) {
                val userDetails: UserDetails = userRepository.findUserByUsername(username)
                    .orElseThrow{UsernameNotFoundException("Пользователь с $username не найден!")}
                val accessToken: String = jwtService.generateAccessToken(userDetails)
                JwtResponse(accessToken, "")
            } else
                MessageResponse("Неверный токен!")
        }
        return MessageResponse("Невалидный токен!")
    }

    override fun refresh(request: JwtRequest): Any {
        if (jwtService.validateRefreshToken(request.refreshToken)) {
            val username: String = jwtService.extractUsernameFromRefreshToken(request.refreshToken)
            val getRefreshToken: String? = tokenRepository.getTokenByUsername(username)
            return if (getRefreshToken != null && getRefreshToken == request.refreshToken) {
                val userDetails: UserDetails = userRepository.findUserByUsername(username)
                    .orElseThrow{UsernameNotFoundException("Пользователь с $username не найден!")}
                val accessToken: String = jwtService.generateAccessToken(userDetails)
                val newRefreshToken: String = jwtService.generateRefreshToken(userDetails)
                tokenRepository.saveTokenAndUsername(username, newRefreshToken)
                JwtResponse(accessToken, newRefreshToken)
            } else
                MessageResponse("Неверный токен!")
        }
        return MessageResponse("Невалидный токен!")
    }

    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findUserByUsername(username)
            .orElseThrow{UsernameNotFoundException("User with username: $username not found")}
}