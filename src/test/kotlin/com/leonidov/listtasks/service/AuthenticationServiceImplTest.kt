package com.leonidov.listtasks.service

import com.leonidov.listtasks.config.jwt.JwtService
import com.leonidov.listtasks.model.User
import com.leonidov.listtasks.model.enums.Role
import com.leonidov.listtasks.persistence.TokenRepository
import com.leonidov.listtasks.persistence.UserRepository
import com.leonidov.listtasks.pojo.*
import com.leonidov.listtasks.service.impl.AuthenticationServiceImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class AuthenticationServiceImplTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var jwtService: JwtService

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @Mock
    lateinit var authenticationManager: AuthenticationManager

    @Mock
    lateinit var tokenRepository: TokenRepository

    @InjectMocks
    lateinit var authenticationService: AuthenticationServiceImpl

    @Captor
    lateinit var userCaptor: ArgumentCaptor<User>

    @Test
    fun register_ifUsernameNotExists_returnValidResponse() {

        val request = AuthenticationRequest("username", "password")
        val encodedPassword = "encode"
        val user = User(request.username, encodedPassword, Role.USER)
        val accessToken = "accessToken"
        val refreshToken = "refreshToken"

        `when`(this.userRepository.findUserBy_username(request.username)).thenReturn(Optional.empty())
        `when`(this.passwordEncoder.encode(request.password)).thenReturn(encodedPassword)
        `when`(this.userRepository.save(userCaptor.capture())).thenReturn(user)
        `when`(this.jwtService.generateAccessToken(user)).thenReturn(accessToken)
        `when`(this.jwtService.generateRefreshToken(user)).thenReturn(refreshToken)

        val response: AuthenticationResponse = this.authenticationService.register(request) as AuthenticationResponse

        assertNotNull(response)
        assertEquals("Bearer", response.type)
        assertEquals(accessToken, response.accessToken)
        assertEquals(refreshToken, response.refreshToken)
        verify(this.userRepository, times(1)).findUserBy_username(request.username)
        verify(this.passwordEncoder, times(1)).encode(request.password)
        verify(this.jwtService, times(1)).generateAccessToken(user)
        verify(this.jwtService, times(1)).generateRefreshToken(user)
        verify(this.tokenRepository, times(1)).saveTokenAndUsername(user.username, refreshToken)
    }

    @Test
    fun register_ifUsernameExists_returnValidResponse() {
        val request = AuthenticationRequest("username", "password")

        `when`(this.userRepository.findUserBy_username(request.username)).thenReturn(Optional.of(User("", "", Role.USER)))

        val response: MessageResponse = this.authenticationService.register(request) as MessageResponse

        assertNotNull(response)
        assertEquals("Username: ${request.username} уже зарегистрирован, выберите другой!", response.message)
        verify(this.userRepository, times(1)).findUserBy_username(request.username)
    }

    @Test
    fun authenticate_ifUserExists_ReturnValidResponse() {
        val request = AuthenticationRequest("username", "password")
        val user = User(request.username, request.password, Role.USER)
        val accessToken = "accessToken"
        val refreshToken = "refreshToken"

        `when`(this.userRepository.findUserBy_username(request.username)).thenReturn(Optional.of(user))
        `when`(this.jwtService.generateAccessToken(user)).thenReturn(accessToken)
        `when`(this.jwtService.generateRefreshToken(user)).thenReturn(refreshToken)

        val response: AuthenticationResponse = this.authenticationService.authenticate(request) as AuthenticationResponse

        assertNotNull(response)
        assertEquals("Bearer", response.type)
        assertEquals(accessToken, response.accessToken)
        assertEquals(refreshToken, response.refreshToken)
        verify(this.userRepository, times(1)).findUserBy_username(request.username)
        verify(this.authenticationManager, times(1)).authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password))
        verify(this.jwtService, times(1)).generateAccessToken(user)
        verify(this.jwtService, times(1)).generateRefreshToken(user)
        verify(this.tokenRepository, times(1)).saveTokenAndUsername(user.username, refreshToken)
    }

    @Test
    fun authenticate_ifUserNotExists_ReturnValidResponse() {
        val request = AuthenticationRequest("username", "password")

        `when`(this.userRepository.findUserBy_username(request.username)).thenReturn(Optional.empty())

        val response: MessageResponse = this.authenticationService.authenticate(request) as MessageResponse

        assertNotNull(response)
        assertEquals("Неверный username или password!", response.message)
    }

    @Test
    fun getAccessToken_ifValidRefreshToken_returnValidResponse() {
        val request = JwtRequest("refreshToken")
        val user = User("username", "password", Role.USER)
        val accessToken = "accessToken"

        `when`(this.jwtService.validateRefreshToken(request.refreshToken)).thenReturn(true)
        `when`(this.jwtService.extractUsernameFromRefreshToken(request.refreshToken)).thenReturn(user.username)
        `when`(this.tokenRepository.getTokenByUsername(user.username)).thenReturn(request.refreshToken)
        `when`(this.userRepository.findUserBy_username(user.username)).thenReturn(Optional.of(user))
        `when`(this.jwtService.generateAccessToken(user)).thenReturn(accessToken)

        val response: JwtResponse = this.authenticationService.getAccessToken(request) as JwtResponse

        assertNotNull(response)
        assertEquals("", response.refreshToken)
        assertEquals(accessToken, response.accessToken)
    }

    @Test
    fun getAccessToken_isNotValidRefreshToken_returnValidResponse() {
        val request = JwtRequest("refreshToken")

        `when`(this.jwtService.validateRefreshToken(request.refreshToken)).thenReturn(false)

        val response: MessageResponse = this.authenticationService.getAccessToken(request) as MessageResponse

        assertNotNull(response)
        assertEquals("Невалидный токен!", response.message)
    }

    @Test
    fun getAccessToken_isNotValidRefreshToken2_returnValidResponse() {
        val request = JwtRequest("refreshToken")
        val username = "username"

        `when`(this.jwtService.validateRefreshToken(request.refreshToken)).thenReturn(true)
        `when`(this.jwtService.extractUsernameFromRefreshToken(request.refreshToken)).thenReturn(username)
        `when`(this.tokenRepository.getTokenByUsername(username)).thenReturn("")

        val response: MessageResponse = this.authenticationService.getAccessToken(request) as MessageResponse

        assertNotNull(response)
        assertEquals("Неверный токен!", response.message)
    }

    @Test
    fun refresh_isValidRefreshToken_ReturnValidResponse() {
        val request = JwtRequest("refreshToken")
        val user = User("username", "", Role.USER)
        val refreshToken = "refreshToken2"
        val accessToken = "accessToken2"

        `when`(this.jwtService.validateRefreshToken(request.refreshToken)).thenReturn(true)
        `when`(this.jwtService.extractUsernameFromRefreshToken(request.refreshToken)).thenReturn(user.username)
        `when`(this.tokenRepository.getTokenByUsername(user.username)).thenReturn("refreshToken")
        `when`(this.userRepository.findUserBy_username(user.username)).thenReturn(Optional.of(user))
        `when`(this.jwtService.generateAccessToken(user)).thenReturn(accessToken)
        `when`(this.jwtService.generateRefreshToken(user)).thenReturn(refreshToken)

        val response: JwtResponse = this.authenticationService.refresh(request) as JwtResponse

        assertNotNull(response)
        assertEquals(accessToken, response.accessToken)
        assertEquals(refreshToken, response.refreshToken)
    }

    @Test
    fun refresh_isNotValidRefreshToken_ReturnValidResponse() {
        val request = JwtRequest("refreshToken")

        `when`(this.jwtService.validateRefreshToken(request.refreshToken)).thenReturn(false)

        val response: MessageResponse = this.authenticationService.refresh(request) as MessageResponse

        assertNotNull(response)
        assertEquals("Невалидный токен!", response.message)
    }

    @Test
    fun refresh_isNotValidRefreshToken2_ReturnValidResponse() {
        val request = JwtRequest("refreshToken")
        val username = "username"
        val refreshToken = "refreshToken2"

        `when`(this.jwtService.validateRefreshToken(request.refreshToken)).thenReturn(true)
        `when`(this.jwtService.extractUsernameFromRefreshToken(request.refreshToken)).thenReturn(username)
        `when`(this.tokenRepository.getTokenByUsername(username)).thenReturn(refreshToken)

        val response: MessageResponse = this.authenticationService.refresh(request) as MessageResponse

        assertNotNull(response)
        assertEquals("Неверный токен!", response.message)
    }

    @Test
    fun loadUserByUsername() {
        val user = User("username", "password", Role.USER)

        `when`(this.userRepository.findUserBy_username(user.username)).thenReturn(Optional.of(user))

        val response = this.authenticationService.loadUserByUsername(user.username)

        assertEquals(user, response)
    }
}