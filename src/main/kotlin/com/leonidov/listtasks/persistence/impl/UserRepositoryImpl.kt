package com.leonidov.listtasks.persistence.impl

import com.leonidov.listtasks.model.User
import com.leonidov.listtasks.persistence.UserRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepositoryImpl (private val mongoTemplate: MongoTemplate) : UserRepository {

    override fun findUserByUsername(username: String): Optional<User> {
        val query = Query()
        query.addCriteria(Criteria.where(username).`is`(username))
        val resultQuery = mongoTemplate.findOne(query, User::class.java, "users")
        return Optional.of(resultQuery!!)
    }

    override fun save(user: User): User {
        return mongoTemplate.save(user, "users")
    }
}