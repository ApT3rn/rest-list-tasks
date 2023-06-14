package com.leonidov.listtasks.service

import com.leonidov.listtasks.model.Task
import com.leonidov.listtasks.persistence.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TaskServiceImpl(private val repository: TaskRepository): TaskService {

    override fun findAll(): MutableList<Task> = repository.findAll()

    override fun findAllByDecided(decided: Boolean): MutableList<Task> = repository.findAllByDecided(decided)

    override fun findById(id: UUID): Optional<Task> = repository.findById(id)

    override fun findByUserCreated(userCreated: UUID): MutableList<Task> = repository.findAllByUserCreated(userCreated)

    override fun saveOrUpdate(task: Task): Task = repository.save(task)

    override fun deleteById(id: UUID) = repository.deleteById(id)
}