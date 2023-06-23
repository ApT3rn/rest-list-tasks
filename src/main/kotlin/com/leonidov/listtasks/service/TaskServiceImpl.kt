package com.leonidov.listtasks.service

import com.leonidov.listtasks.model.Task
import com.leonidov.listtasks.persistence.TaskRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TaskServiceImpl(private val repository: TaskRepository): TaskService {
    override fun findAllByStatus(status: String): MutableList<Task> = repository.findAllByStatus(status)
    override fun findAllByStatusAndWorked(status: String, username: String): MutableList<Task> = repository.findAllByStatusAndWorked(status, username)
    override fun findById(id: UUID): Optional<Task> = repository.findById(id)
    override fun findAllByCreator(username: String): MutableList<Task> = repository.findAllByCreator(username)
    override fun saveOrUpdate(task: Task): Task = repository.save(task)
}