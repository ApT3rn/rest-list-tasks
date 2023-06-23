package com.leonidov.listtasks.persistence

import com.leonidov.listtasks.model.Task
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TaskRepository : MongoRepository<Task, UUID> {
    fun findAllByStatus(status: String): MutableList<Task>
    fun findAllByStatusAndWorked(status: String, username: String): MutableList<Task>
    fun findAllByCreator(username: String): MutableList<Task>
}