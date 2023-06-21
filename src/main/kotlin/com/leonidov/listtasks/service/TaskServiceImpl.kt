package com.leonidov.listtasks.service

import com.leonidov.listtasks.model.Task
import com.leonidov.listtasks.model.enums.Status
import com.leonidov.listtasks.persistence.TaskRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TaskServiceImpl(private val repository: TaskRepository): TaskService {
    override fun findAllByStatus(status: Status): MutableList<Task> = repository.findAllByStatus(status)
    override fun findAllByStatusAndWorked(status: Status, username: String): MutableList<Task> = repository.findAllByStatusAndWorked(status, username)

    override fun findById(id: UUID): Optional<Task> = repository.findById(id)
    override fun findAllByCreated(created: String): MutableList<Task> = repository.findAllByCreated(created)
    override fun saveOrUpdate(task: Task): Task = repository.save(task)
    override fun deleteById(id: UUID) = repository.deleteById(id)
}