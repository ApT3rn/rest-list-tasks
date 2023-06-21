package com.leonidov.listtasks.persistence

import com.leonidov.listtasks.model.Task
import com.leonidov.listtasks.model.enums.Status
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TaskRepository : MongoRepository<Task, UUID> {
    fun findAllByStatus(status: Status): MutableList<Task>
    fun findAllByStatusAndWorked(status: Status, username: String): MutableList<Task>
    fun findAllByCreated(username: String): MutableList<Task>
}