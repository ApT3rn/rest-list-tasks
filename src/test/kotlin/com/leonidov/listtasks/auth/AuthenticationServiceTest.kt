package com.leonidov.listtasks.auth

import com.leonidov.listtasks.config.jwt.JwtService
import com.leonidov.listtasks.model.User
import com.leonidov.listtasks.model.enums.Role
import com.leonidov.listtasks.persistence.UserRepository
import com.leonidov.listtasks.pojo.AuthenticationRequest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class AuthenticationServiceTest {

    @Mock
    lateinit var repository: UserRepository

    @Mock
    lateinit var jwtService: JwtService

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @Mock
    lateinit var authenticationManager: AuthenticationManager

    @Mock
    lateinit var redisTemplate: RedisTemplate<String, String>

    @InjectMocks
    lateinit var authenticationService: AuthenticationService

    @Captor
    lateinit var userCaptor: ArgumentCaptor<User>

    @Test
    fun register_ifNewUser_returnValidResponse() {

        val request = AuthenticationRequest("username", "password")
        val encodedPassword = "encode"
        val user = User(request.username, encodedPassword, Role.USER)
        val accessToken = "accessToken"
        val refreshToken = "refreshToken"

        `when`(this.repository.findUserBy_username(request.username)).thenReturn(Optional.empty())
        `when`(this.passwordEncoder.encode(request.password)).thenReturn(encodedPassword)
        `when`(this.repository.save(userCaptor.capture())).thenReturn(user)
        `when`(this.jwtService.generateAccessToken(user)).thenReturn(accessToken)
        `when`(this.jwtService.generateRefreshToken(user)).thenReturn(refreshToken)
        doNothing().`when`(this.redisTemplate.opsForValue().set(user.username, refreshToken))

        val response: Any = authenticationService.register(request)

        println(response)
        assertNotNull(response)
    }

    @Test
    fun authenticate() {
    }

    @Test
    fun getAccessToken() {
    }

    @Test
    fun refresh() {
    }

    @Test
    fun loadUserByUsername() {
    }
}