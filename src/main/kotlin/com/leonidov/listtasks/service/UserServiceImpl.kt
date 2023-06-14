package com.leonidov.listtasks.service

import com.leonidov.listtasks.model.User
import com.leonidov.listtasks.persistence.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override fun findAll(): MutableList<User> = userRepository.findAll()

    override fun findById(id: UUID): Optional<User> = userRepository.findById(id)

    override fun saveOrUpdate(user: User): User = userRepository.save(user)

    override fun deleteById(id: UUID) = userRepository.deleteById(id)
}