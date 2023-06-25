package com.leonidov.listtasks.controller

import com.leonidov.listtasks.model.Task
import com.leonidov.listtasks.model.enums.Status
import com.leonidov.listtasks.pojo.TaskRequest
import com.leonidov.listtasks.service.TaskService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.security.Principal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@RestController
@RequestMapping("/api/v1/tasks")
class TasksController(private val taskService: TaskService) {

    @GetMapping
    fun handleGetAllTasks(): ResponseEntity<*> {
        return ResponseEntity.ok(taskService.findAllByStatus(Status.CREATED.name))
    }

    @GetMapping("/created")
    fun handleGetAllTasksUserCreated(principal: Principal): ResponseEntity<*> {
        return ResponseEntity.ok(taskService.findAllByCreator(principal.name))
    }

    @GetMapping("/progress")
    fun handleGetAllTasksInProgress(principal: Principal): ResponseEntity<*> {
        return ResponseEntity.ok(taskService.findAllByStatusAndWorked(Status.PROGRESS.name, principal.name))
    }

    @GetMapping("/completed")
    fun handleGetAllTasksCompleted(principal: Principal): ResponseEntity<*> {
        return ResponseEntity.ok(taskService.findAllByStatusAndWorked(Status.COMPLETED.name, principal.name))
    }

    @PostMapping
    fun handleSaveNewTask(
        @RequestBody request: TaskRequest,
        principal: Principal): ResponseEntity<Any> {
        if (request.details.isBlank())
            return ResponseEntity.badRequest().build()
        val taskSaved: Task = taskService.saveOrUpdate(Task(request.details, principal.name))
        val location: URI =
            ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(taskSaved.id).toUri()
        return ResponseEntity.created(location).build()
    }

    @GetMapping("/{id}")
    fun handleGetTask(@PathVariable("id") id: UUID): ResponseEntity<Any> {
        val task: Optional<Task> = taskService.findById(id)
        if (task.isEmpty)
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok(task)
    }

    @PostMapping("/{id}")
    fun handleUpdateTask(
        @PathVariable("id") id: UUID,
        @RequestBody request: TaskRequest): ResponseEntity<Any> {
        if (request.details.isBlank())
            return ResponseEntity.badRequest().build()
        val task: Optional<Task> = taskService.findById(id)
        if (task.isEmpty)
            return ResponseEntity.notFound().build()
        task.get().details = request.details
        return ResponseEntity.ok(taskService.saveOrUpdate(task.get()))

    }

    @PostMapping("/{id}/progress")
    fun handleTakeTaskToWork(@PathVariable("id") id: UUID, principal: Principal): ResponseEntity<Any> {
        val task: Optional<Task> = taskService.findById(id)
        if (task.isEmpty || task.get().status != Status.CREATED.name)
            return ResponseEntity.notFound().build()
        task.get().status = Status.PROGRESS.name
        task.get().worked = principal.name
        return ResponseEntity.ok(taskService.saveOrUpdate(task.get()))
    }

    @PostMapping("/{id}/complete")
    fun handleTaskToCompleted(@PathVariable("id") id: UUID): ResponseEntity<Any> {
        val task: Optional<Task> = taskService.findById(id)
        if (task.isEmpty)
            return ResponseEntity.notFound().build()
        task.get().status = Status.COMPLETED.name
        task.get().completed_at = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm:ss a"))
        return ResponseEntity.ok(taskService.saveOrUpdate(task.get()))
    }
}