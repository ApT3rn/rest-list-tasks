package com.leonidov.listtasks.persistence

import com.leonidov.listtasks.model.Task
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TaskRepository : MongoRepository<Task, UUID> {
    fun findAllByDecided(decided: Boolean): MutableList<Task>
    fun findAllByUserCreated(id: UUID): MutableList<Task>
}