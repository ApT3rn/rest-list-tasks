package com.leonidov.listtasks.service

import com.leonidov.listtasks.model.Task
import com.leonidov.listtasks.model.enums.Status
import java.util.*

interface TaskService {
    fun findAllByStatus(status: Status): MutableList<Task>
    fun findAllByStatusAndWorked(status: Status, username: String): MutableList<Task>
    fun findById(id: UUID): Optional<Task>
    fun findAllByCreated(created: String): MutableList<Task>
    fun saveOrUpdate(task: Task): Task
    fun deleteById(id: UUID)
}