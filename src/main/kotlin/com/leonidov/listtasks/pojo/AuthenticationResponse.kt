package com.leonidov.listtasks.pojo

class AuthenticationResponse (
    val type: String, val accessToken: String, val refreshToken: String) {
    constructor(accessToken: String, refreshToken: String):
            this("Bearer", accessToken, refreshToken)
}