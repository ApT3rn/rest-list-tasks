package com.leonidov.listtasks.controller

import com.leonidov.listtasks.auth.AuthenticationRequest
import com.leonidov.listtasks.auth.AuthenticationResponse
import com.leonidov.listtasks.auth.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class UsersController (private val authenticationService: AuthenticationService) {

    @GetMapping()
    fun handleMainUrl(): String {
        return "Привет!"
    }

    @PostMapping("/register")
    fun handleCreateNewUser(@RequestBody request: AuthenticationRequest): ResponseEntity<*> {
        return ResponseEntity.ok(authenticationService.register(request))
    }

    @PostMapping("/authenticate")
    fun handleAuthenticateUser(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.authenticate(request))
    }
}
