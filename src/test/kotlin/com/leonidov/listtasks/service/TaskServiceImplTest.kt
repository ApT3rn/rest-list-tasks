package com.leonidov.listtasks.service

import com.leonidov.listtasks.model.Task
import com.leonidov.listtasks.model.enums.Status
import com.leonidov.listtasks.persistence.TaskRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import java.util.concurrent.TimeUnit

@ExtendWith(MockitoExtension::class)
@Timeout(value = 3L, unit = TimeUnit.SECONDS)
internal class TaskServiceImplTest {

    @Mock
    lateinit var repository: TaskRepository

    @InjectMocks
    lateinit var taskService: TaskServiceImpl


    @Test
    fun findAllByStatus_ifStatusCreatedAndZeroTasks_returnValidResponse() {

        `when`(this.repository.findAllByStatus(Status.CREATED.name)).thenReturn(Collections.emptyList())

        val response = taskService.findAllByStatus(Status.CREATED.name)

        assertEquals(0, response.size)
        verify(this.repository, times(1)).findAllByStatus(Status.CREATED.name)
        verify(this.repository, times(0)).findAllByStatus(Status.PROGRESS.name)
        verify(this.repository, times(0)).findAllByStatus(Status.COMPLETED.name)
    }

    @Test
    fun findAllByStatus_ifStatusProgressAndZeroTasks_returnValidResponse() {

        `when`(this.repository.findAllByStatus(Status.PROGRESS.name)).thenReturn(Collections.emptyList())

        val response = taskService.findAllByStatus(Status.PROGRESS.name)

        assertEquals(0, response.size)
        verify(this.repository, times(0)).findAllByStatus(Status.CREATED.name)
        verify(this.repository, times(1)).findAllByStatus(Status.PROGRESS.name)
        verify(this.repository, times(0)).findAllByStatus(Status.COMPLETED.name)
    }

    @Test
    fun findAllByStatus_ifStatusCompletedAndZeroTasks_returnValidResponse() {

        `when`(this.repository.findAllByStatus(Status.COMPLETED.name)).thenReturn(Collections.emptyList())

        val response = taskService.findAllByStatus(Status.COMPLETED.name)

        assertEquals(0, response.size)
        verify(this.repository, times(0)).findAllByStatus(Status.CREATED.name)
        verify(this.repository, times(0)).findAllByStatus(Status.PROGRESS.name)
        verify(this.repository, times(1)).findAllByStatus(Status.COMPLETED.name)
    }

    @Test
    fun findAllByStatus_ifStatusCreated_returnValidResponse() {

        val list = mutableListOf(
            Task("task1", "user1"),
            Task("task2", "user2")
            )

        `when`(this.repository.findAllByStatus(Status.CREATED.name)).thenReturn(list)

        val response = taskService.findAllByStatus(Status.CREATED.name)

        assertNotNull(response)
        assertEquals(2, response.size)
        assertEquals(response, list)
        assertEquals("task1", response[0].details)
        assertEquals("task2", response[1].details)
        assertEquals("user1", response[0].creator)
        assertEquals("user2", response[1].creator)
        verify(this.repository, times(1)).findAllByStatus(Status.CREATED.name)
        verify(this.repository, times(0)).findAllByStatus(Status.PROGRESS.name)
        verify(this.repository, times(0)).findAllByStatus(Status.COMPLETED.name)
    }

    @Test
    fun findAllByStatus_ifStatusProgress_returnValidResponse() {

        val list = mutableListOf(
            Task(UUID.randomUUID(), "task1", Status.PROGRESS.name, "user1", "", "", ""),
            Task(UUID.randomUUID(), "task2", Status.PROGRESS.name, "user2", "", "", "")
        )

        `when`(this.repository.findAllByStatus(Status.PROGRESS.name)).thenReturn(list)

        val response = taskService.findAllByStatus(Status.PROGRESS.name)

        assertNotNull(response)
        assertEquals(2, response.size)
        assertEquals(response, list)
        assertEquals("task1", response[0].details)
        assertEquals("task2", response[1].details)
        assertEquals("user1", response[0].creator)
        assertEquals("user2", response[1].creator)
        assertEquals(Status.PROGRESS.name, response[0].status)
        assertEquals(Status.PROGRESS.name, response[1].status)
        verify(this.repository, times(0)).findAllByStatus(Status.CREATED.name)
        verify(this.repository, times(1)).findAllByStatus(Status.PROGRESS.name)
        verify(this.repository, times(0)).findAllByStatus(Status.COMPLETED.name)
    }

    @Test
    fun findAllByStatus_ifStatusCompleted_returnValidResponse() {

        val list = mutableListOf(
            Task(UUID.randomUUID(), "task1", Status.COMPLETED.name, "user1", "", "", ""),
            Task(UUID.randomUUID(), "task2", Status.COMPLETED.name, "user2", "", "", "")
        )

        `when`(this.repository.findAllByStatus(Status.COMPLETED.name)).thenReturn(list)

        val response = taskService.findAllByStatus(Status.COMPLETED.name)

        assertNotNull(response)
        assertEquals(2, response.size)
        assertEquals(response, list)
        assertEquals("task1", response[0].details)
        assertEquals("task2", response[1].details)
        assertEquals("user1", response[0].creator)
        assertEquals("user2", response[1].creator)
        assertEquals(Status.COMPLETED.name, response[0].status)
        assertEquals(Status.COMPLETED.name, response[1].status)
        verify(this.repository, times(0)).findAllByStatus(Status.CREATED.name)
        verify(this.repository, times(0)).findAllByStatus(Status.PROGRESS.name)
        verify(this.repository, times(1)).findAllByStatus(Status.COMPLETED.name)
    }

    @Test
    fun findAllByStatusAndWorked_ifStatusCreated_ReturnValidResponse() {

        `when`(this.repository.findAllByStatusAndWorked(Status.CREATED.name, "")).thenReturn(Collections.emptyList())

        val response = this.taskService.findAllByStatusAndWorked(Status.CREATED.name, "")

        assertEquals(0, response.size)
        verify(this.repository, times(1)).findAllByStatusAndWorked(Status.CREATED.name, "")
        verify(this.repository, times(0)).findAllByStatusAndWorked(Status.PROGRESS.name, "")
        verify(this.repository, times(0)).findAllByStatusAndWorked(Status.COMPLETED.name, "")
    }

    @Test
    fun findAllByStatusAndWorked_ifStatusProgressAndZeroTask_ReturnValidResponse() {

        `when`(this.repository.findAllByStatusAndWorked(Status.PROGRESS.name, "")).thenReturn(Collections.emptyList())

        val response = this.taskService.findAllByStatusAndWorked(Status.PROGRESS.name, "")

        assertEquals(0, response.size)
        verify(this.repository, times(0)).findAllByStatusAndWorked(Status.CREATED.name, "")
        verify(this.repository, times(1)).findAllByStatusAndWorked(Status.PROGRESS.name, "")
        verify(this.repository, times(0)).findAllByStatusAndWorked(Status.COMPLETED.name, "")
    }

    @Test
    fun findAllByStatusAndWorked_ifStatusCompletedAndZeroTask_ReturnValidResponse() {

        `when`(this.repository.findAllByStatusAndWorked(Status.COMPLETED.name, "")).thenReturn(Collections.emptyList())

        val response = this.taskService.findAllByStatusAndWorked(Status.COMPLETED.name, "")

        assertEquals(0, response.size)
        verify(this.repository, times(0)).findAllByStatusAndWorked(Status.CREATED.name, "")
        verify(this.repository, times(0)).findAllByStatusAndWorked(Status.PROGRESS.name, "")
        verify(this.repository, times(1)).findAllByStatusAndWorked(Status.COMPLETED.name, "")
    }

    @Test
    fun findAllByStatusAndWorked_ifStatusProgress_ReturnValidResponse() {

        val list = mutableListOf(
            Task(UUID.randomUUID(), "task1", Status.PROGRESS.name, "user1", "", "", ""),
            Task(UUID.randomUUID(), "task2", Status.PROGRESS.name, "user2", "", "", "")
        )

        `when`(this.repository.findAllByStatusAndWorked(Status.PROGRESS.name, "")).thenReturn(list)

        val response = taskService.findAllByStatusAndWorked(Status.PROGRESS.name, "")

        assertNotNull(response)
        assertEquals(2, response.size)
        assertEquals(response, list)
        assertEquals("task1", response[0].details)
        assertEquals("task2", response[1].details)
        assertEquals("user1", response[0].creator)
        assertEquals("user2", response[1].creator)
        assertEquals(Status.PROGRESS.name, response[0].status)
        assertEquals(Status.PROGRESS.name, response[1].status)
        verify(this.repository, times(0)).findAllByStatusAndWorked(Status.CREATED.name, "")
        verify(this.repository, times(1)).findAllByStatusAndWorked(Status.PROGRESS.name, "")
        verify(this.repository, times(0)).findAllByStatusAndWorked(Status.COMPLETED.name, "")
    }

    @Test
    fun findAllByStatusAndWorked_ifStatusCompleted_ReturnValidResponse() {

        val list = mutableListOf(
            Task(UUID.randomUUID(), "task1", Status.COMPLETED.name, "user1", "", "", ""),
            Task(UUID.randomUUID(), "task2", Status.COMPLETED.name, "user2", "", "", "")
        )

        `when`(this.repository.findAllByStatusAndWorked(Status.COMPLETED.name, "")).thenReturn(list)

        val response = taskService.findAllByStatusAndWorked(Status.COMPLETED.name, "")

        assertNotNull(response)
        assertEquals(2, response.size)
        assertEquals(response, list)
        assertEquals("task1", response[0].details)
        assertEquals("task2", response[1].details)
        assertEquals("user1", response[0].creator)
        assertEquals("user2", response[1].creator)
        assertEquals(Status.COMPLETED.name, response[0].status)
        assertEquals(Status.COMPLETED.name, response[1].status)
        verify(this.repository, times(0)).findAllByStatusAndWorked(Status.CREATED.name, "")
        verify(this.repository, times(0)).findAllByStatusAndWorked(Status.PROGRESS.name, "")
        verify(this.repository, times(1)).findAllByStatusAndWorked(Status.COMPLETED.name, "")
    }

    @Test
    fun findById_ifTasksExists_ReturnValidResponse() {

        val task = Task("", "")

        `when`(this.repository.findById(task.id!!)).thenReturn(Optional.of(task))

        val response = this.taskService.findById(task.id!!)

        assertTrue(response.isPresent)
        assertFalse(response.isEmpty)
        assertEquals(task, response.get())
        verify(this.repository, times(1)).findById(task.id!!)
    }

    @Test
    fun findById_ifTasksNotExists_ReturnValidResponse() {

        val id: UUID = UUID.randomUUID()

        `when`(this.repository.findById(id)).thenReturn(Optional.empty())

        val response = this.taskService.findById(id)

        assertTrue(response.isEmpty)
        assertFalse(response.isPresent)
        verify(this.repository, times(1)).findById(id)
    }

    @Test
    fun findAllByCreator_ifTasksExists_ReturnValidResponse() {

        val creator = "username"
        val list = mutableListOf(
            Task(UUID.randomUUID(), "task1", Status.CREATED.name, creator, "", "", ""),
            Task(UUID.randomUUID(), "task2", Status.CREATED.name, creator, "", "", "")
        )

        `when`(this.repository.findAllByCreator(creator)).thenReturn(list)

        val response = this.taskService.findAllByCreator(creator)

        assertNotNull(response)
        assertEquals(2, response.size)
        assertEquals(list, response)
        assertEquals("task1", response[0].details)
        assertEquals("task2", response[1].details)
        assertEquals(creator, response[0].creator)
        assertEquals(creator, response[1].creator)
        verify(this.repository, times(1)).findAllByCreator(creator)
    }

    @Test
    fun findAllByCreator_ifTasksNotExists_ReturnValidResponse() {

        val creator = "username"

        `when`(this.repository.findAllByCreator(creator)).thenReturn(Collections.emptyList())

        val response = this.taskService.findAllByCreator(creator)

        assertNotNull(response)
        assertEquals(0, response.size)
        verify(this.repository, times(1)).findAllByCreator(creator)
    }

    @Test
    fun saveOrUpdate() {

        val task = Task("task1", "")

        `when`(this.repository.save(task)).thenReturn(task)

        val response = this.taskService.saveOrUpdate(task)

        assertEquals(task, response)
        verify(this.repository, times(1)).save(task)
    }
}