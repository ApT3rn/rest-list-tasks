package com.leonidov.listtasks.service

import com.leonidov.listtasks.model.Task
import java.util.*

interface TaskService {
    fun findAllByStatus(status: String): MutableList<Task>
    fun findAllByStatusAndWorked(status: String, username: String): MutableList<Task>
    fun findById(id: UUID): Optional<Task>
    fun findAllByCreator(username: String): MutableList<Task>
    fun saveOrUpdate(task: Task): Task
}