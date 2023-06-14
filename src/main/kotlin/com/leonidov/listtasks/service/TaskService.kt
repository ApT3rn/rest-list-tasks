package com.leonidov.listtasks.service

import com.leonidov.listtasks.model.Task
import org.springframework.stereotype.Service
import java.util.*

interface TaskService {
    fun findAll(): MutableList<Task>
    fun findAllByDecided(decided: Boolean): MutableList<Task>
    fun findById(id: UUID): Optional<Task>
    fun findByUserCreated(userCreated: UUID): MutableList<Task>
    fun saveOrUpdate(task: Task): Task
    fun deleteById(id: UUID)
}