package com.leonidov.listtasks.controller

import com.fasterxml.jackson.annotation.JsonIgnore
import com.leonidov.listtasks.model.Task
import com.leonidov.listtasks.service.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/tasks")
class TasksController(@Autowired private val taskService: TaskService) {

    @GetMapping
    fun handleGetAllTasks(): ResponseEntity<*> {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(taskService.findAll())
    }

    @PostMapping
    fun handleSaveNewTask(
        @RequestBody details: String,
        @RequestBody userCreated: String): ResponseEntity<*> {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(taskService.saveOrUpdate(
                    Task(details, UUID.fromString(userCreated))))
    }

    @GetMapping("/{id}")
    fun handleGetTask(@PathVariable("id") id: UUID): ResponseEntity<*> {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(taskService.findById(id))
    }

    @DeleteMapping("/{id}")
    fun handleDeleteTask(@PathVariable("id") id: UUID): ResponseEntity<*> {
        return if (taskService.findById(id).isPresent) {
            taskService.deleteById(id)
            ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Успешно удалено")
        } else {
            ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Такой задачи не существует")
        }
    }
}