package com.alejandro.spock.core.gtd.task.controller

import com.alejandro.spock.core.gtd.task.dto.CreateTaskRequest
import com.alejandro.spock.core.gtd.task.dto.TaskResponse
import com.alejandro.spock.core.gtd.task.dto.UpdateTaskRequest
import com.alejandro.spock.core.gtd.task.service.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class TaskController(
	private val taskService: TaskService,
) {
	@GetMapping("/tasks")
	fun listTasks(): List<TaskResponse> =
		taskService.listTasks()

	@PostMapping("/tasks")
	@ResponseStatus(HttpStatus.CREATED)
	fun createTask(@Valid @RequestBody request: CreateTaskRequest): TaskResponse =
		taskService.createTask(request)

	@GetMapping("/tasks/{id}")
	fun getTask(@PathVariable id: UUID): TaskResponse =
		taskService.getTask(id)

	@PatchMapping("/tasks/{id}")
	fun updateTask(@PathVariable id: UUID, @Valid @RequestBody request: UpdateTaskRequest): TaskResponse =
		taskService.updateTask(id, request)
}
