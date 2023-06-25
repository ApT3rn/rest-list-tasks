package com.leonidov.listtasks.service

import com.leonidov.listtasks.pojo.AuthenticationRequest
import com.leonidov.listtasks.pojo.JwtRequest

interface AuthenticationService {
    fun register(request: AuthenticationRequest): Any
    fun authenticate(request: AuthenticationRequest): Any
    fun getAccessToken(request: JwtRequest): Any
    fun refresh(request: JwtRequest): Any
}