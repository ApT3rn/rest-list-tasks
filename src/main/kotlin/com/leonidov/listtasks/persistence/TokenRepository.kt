package com.leonidov.listtasks.persistence

interface TokenRepository {

    fun getTokenByUsername(username: String): String?
    fun saveTokenAndUsername(username: String, refreshToken: String)
}