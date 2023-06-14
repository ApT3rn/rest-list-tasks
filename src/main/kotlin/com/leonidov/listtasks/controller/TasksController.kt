package com.leonidov.listtasks.controller

import com.leonidov.listtasks.model.Task
import com.leonidov.listtasks.service.TaskService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/tasks")
class TasksController(private val taskService: TaskService) {

    @GetMapping
    fun handleGetAllTasks(): ResponseEntity<*> {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(taskService.findAll())
    }

    @PostMapping
    fun handleSaveNewTask(
        @RequestBody _task: Task): ResponseEntity<*> {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                taskService.saveOrUpdate(
                    Task(_task.details, _task.userCreated)))
    }

    @GetMapping("/{id}")
    fun handleGetTask(@PathVariable("id") id: UUID): ResponseEntity<*> {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(taskService.findById(id))
    }

    @PutMapping("/{id}")
    fun handleUpdateTask(
        @PathVariable("id") id: UUID,
        @RequestBody _task: Task): ResponseEntity<*> {
        return if (taskService.findById(id).isPresent) {
            taskService.saveOrUpdate(Task(id, _task.details, _task.decided, _task.userCreated))
            ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Успешно изменено")
        } else {
            ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Такой задачи не существует")
        }
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