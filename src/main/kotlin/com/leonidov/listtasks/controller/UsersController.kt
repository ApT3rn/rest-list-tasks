package com.leonidov.listtasks.controller

import com.leonidov.listtasks.pojo.AuthenticationRequest
import com.leonidov.listtasks.pojo.JwtRequest
import com.leonidov.listtasks.service.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/auth")
class UsersController (private val authenticationService: AuthenticationService) {

    @PostMapping("/register")
    fun handleCreateNewUser(@RequestBody request: AuthenticationRequest): ResponseEntity<*> {
        return ResponseEntity.ok(authenticationService.register(request))
    }

    @PostMapping("/authenticate")
    fun handleAuthenticateUser(@RequestBody request: AuthenticationRequest): ResponseEntity<*> {
        return ResponseEntity.ok(authenticationService.authenticate(request))
    }

    @PostMapping("token")
    fun getNewAccessToken(@RequestBody request: JwtRequest): ResponseEntity<*> {
        return ResponseEntity.ok(authenticationService.getAccessToken(request))
    }

    @PostMapping("refresh")
    fun getNewRefreshToken(@RequestBody request: JwtRequest): ResponseEntity<*> {
        return ResponseEntity.ok(authenticationService.refresh(request))
    }
}
