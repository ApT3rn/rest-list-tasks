package com.leonidov.listtasks.persistence

import com.leonidov.listtasks.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : MongoRepository<User, UUID> {
}
