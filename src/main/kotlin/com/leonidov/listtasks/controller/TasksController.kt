package com.leonidov.listtasks.controller

import com.leonidov.listtasks.model.Task
import com.leonidov.listtasks.model.enums.Status
import com.leonidov.listtasks.service.TaskService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/api/v1/tasks")
class TasksController(private val taskService: TaskService) {

    @GetMapping
    fun handleGetAllTasks(): ResponseEntity<*> {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(taskService.findAllByStatus(Status.CREATED))
    }

    @GetMapping("/created")
    fun handleGetAllTasksUserCreated(principal: Principal): ResponseEntity<*> {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(taskService.findAllByCreated(principal.name))
    }

    @GetMapping("/progress")
    fun handleGetAllTasksInProgress(principal: Principal): ResponseEntity<*> {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(taskService.findAllByStatusAndWorked(Status.PROGRESS, principal.name))
    }

    @GetMapping("/completed")
    fun handleGetAllTasksCompleted(principal: Principal): ResponseEntity<*> {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(taskService.findAllByStatusAndWorked(Status.PROGRESS, principal.name))
    }

    @PostMapping
    fun handleSaveNewTask(@RequestBody details: String,
                          principal: Principal): ResponseEntity<*> {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(taskService.saveOrUpdate(
                    Task(details, principal.name)))
    }

    @GetMapping("/{id}")
    fun handleGetTask(@PathVariable("id") id: UUID): ResponseEntity<*> {
        val task:Optional<Task> = taskService.findById(id)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(task)
    }

    @PostMapping("/{id}")
    fun handleTakeTaskToWork(@PathVariable("id") id: UUID, principal: Principal): ResponseEntity<*> {
        val task: Task = taskService.findById(id).orElse(null)
        return run {
            task.status = Status.PROGRESS
            task.worked = principal.name
            taskService.saveOrUpdate(task)
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(task)
        }
    }

    @PutMapping("/{id}")
    fun handleUpdateTask(@PathVariable("id") id: UUID,
        @RequestBody _task: Task): ResponseEntity<*> {
        return if (taskService.findById(id).isPresent) {
            taskService.saveOrUpdate(_task)
            ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Успешно изменено")
        } else
            ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Такой задачи не существует")
    }

    @PostMapping("/{id}/completed")
    fun handleTaskToCompleted(@PathVariable("id") id: UUID): ResponseEntity<*> {
        val task: Task = taskService.findById(id).orElse(null)
        return run {
            task.status = Status.COMPLETED
            taskService.saveOrUpdate(task)
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(task)
        }
    }
}