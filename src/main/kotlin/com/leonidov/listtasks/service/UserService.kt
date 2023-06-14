package com.leonidov.listtasks.service

import com.leonidov.listtasks.model.User
import java.util.*

interface UserService {
    fun findAll(): MutableList<User>
    fun findById(id: UUID): Optional<User>
    fun saveOrUpdate(user: User): User
    fun deleteById(id: UUID)
}