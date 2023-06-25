package com.leonidov.listtasks.persistence.impl

import com.leonidov.listtasks.model.Task
import com.leonidov.listtasks.persistence.TaskRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class TaskRepositoryImpl (private val mongoTemplate: MongoTemplate) : TaskRepository {

    override fun findAllByStatus(status: String): MutableList<Task> {
        val query = Query()
        query.addCriteria(Criteria.where("status").`is`(status))
        return mongoTemplate.find(query, Task::class.java, "tasks")
    }

    override fun findAllByStatusAndWorked(status: String, username: String): MutableList<Task> {
        val query = Query()
        query.addCriteria(Criteria.where("status").`is`(status))
        query.addCriteria(Criteria.where("worked").`is`(username))
        return mongoTemplate.find(query, Task::class.java, "tasks")
    }

    override fun findAllByCreator(username: String): MutableList<Task> {
        val query = Query()
        query.addCriteria(Criteria.where("creator").`is`(username))
        return mongoTemplate.find(query, Task::class.java, "tasks")
    }

    override fun findById(id: UUID): Optional<Task> {
        val query = Query()
        query.addCriteria(Criteria.where("_id").`is`(id))
        val queryResult = mongoTemplate.findOne(query, Task::class.java, "tasks")
        return Optional.of(queryResult!!)
    }

    override fun save(task: Task): Task {
        return mongoTemplate.save(task, "tasks")
    }
}