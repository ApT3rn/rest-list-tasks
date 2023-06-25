package com.leonidov.listtasks.persistence.impl

import com.leonidov.listtasks.persistence.TokenRepository
import org.springframework.context.annotation.Lazy
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class TokenRepositoryImpl(@Lazy private val redisTemplate: RedisTemplate<String, String>) : TokenRepository {

    override fun getTokenByUsername(username: String): String? {
        return redisTemplate.opsForValue().get(username)
    }

    override fun saveTokenAndUsername(username: String, refreshToken: String) {
        redisTemplate.opsForValue().set(username, refreshToken)
    }
}