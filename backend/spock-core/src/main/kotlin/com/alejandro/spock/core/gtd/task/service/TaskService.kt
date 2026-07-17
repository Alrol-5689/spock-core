package com.alejandro.spock.core.gtd.task.service

import com.alejandro.spock.core.gtd.task.dto.CreateTaskRequest
import com.alejandro.spock.core.gtd.task.dto.TaskResponse
import com.alejandro.spock.core.gtd.task.dto.UpdateTaskRequest
import com.alejandro.spock.core.gtd.task.model.Task
import com.alejandro.spock.core.gtd.task.model.TaskPriority
import com.alejandro.spock.core.gtd.task.model.TaskStatus
import com.alejandro.spock.core.gtd.task.repository.TaskRepository
import com.alejandro.spock.core.shared.model.entity.BaseEntity
import com.alejandro.spock.core.shared.model.entity.EntityType
import com.alejandro.spock.core.shared.repository.BaseEntityRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class TaskService(
	private val taskRepository: TaskRepository,
	private val baseEntityRepository: BaseEntityRepository,
) {
	@Transactional(readOnly = true)
	fun listTasks(): List<TaskResponse> =
		taskRepository.findAll().map { it.toResponse() }.sortedBy { it.title }

	@Transactional(readOnly = true)
	fun listOpenTasks(): List<TaskResponse> =
		taskRepository.findAllByStatusIn(activeStatuses)
			.sortedWith(taskComparator)
			.map { it.toResponse() }

	@Transactional(readOnly = true)
	fun listTodayTasks(date: LocalDate = LocalDate.now(ZoneOffset.UTC)): List<TaskResponse> {
		val endOfDay = date.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC)
		return taskRepository.findAllByStatusIn(activeStatuses)
			.filter { task ->
				task.dueAt?.isBefore(endOfDay) == true || task.scheduledAt?.toLocalDate() == date
			}
			.sortedWith(taskComparator)
			.map { it.toResponse() }
	}

	@Transactional
	fun createTask(request: CreateTaskRequest): TaskResponse {
		val entity = baseEntityRepository.save(
			BaseEntity(entityType = EntityType.TASK, title = request.title, summary = request.summary, status = request.status.name),
		)
		return taskRepository.save(
			Task(entity = entity, status = request.status, priority = request.priority, dueAt = request.dueAt, scheduledAt = request.scheduledAt),
		).toResponse()
	}

	@Transactional(readOnly = true)
	fun getTask(id: UUID): TaskResponse =
		task(id).toResponse()

	@Transactional
	fun updateTask(id: UUID, request: UpdateTaskRequest): TaskResponse {
		val task = task(id)
		request.title?.let { task.entity.title = it }
		request.summary?.let { task.entity.summary = it }
		request.status?.let {
			task.status = it
			task.entity.status = it.name
		}
		request.priority?.let { task.priority = it }
		request.dueAt?.let { task.dueAt = it }
		request.scheduledAt?.let { task.scheduledAt = it }
		request.completedAt?.let { task.completedAt = it }
		return taskRepository.save(task).toResponse()
	}

	private fun task(id: UUID): Task =
		taskRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Task $id not found") }

	private val activeStatuses = listOf(TaskStatus.OPEN, TaskStatus.IN_PROGRESS, TaskStatus.WAITING)

	private val taskComparator =
		compareByDescending<Task> { it.priority?.rank ?: 0 }
			.thenBy { it.dueAt ?: OffsetDateTime.MAX }
			.thenBy { it.scheduledAt ?: OffsetDateTime.MAX }
			.thenBy { it.entity.title }

	private val TaskPriority.rank: Int
		get() = when (this) {
			TaskPriority.LOW -> 1
			TaskPriority.MEDIUM -> 2
			TaskPriority.HIGH -> 3
			TaskPriority.URGENT -> 4
		}

	private fun Task.toResponse(): TaskResponse =
		TaskResponse(id = requiredId(), title = entity.title, summary = entity.summary, status = status, priority = priority, dueAt = dueAt, scheduledAt = scheduledAt, completedAt = completedAt, createdAt = entity.createdAt, updatedAt = entity.updatedAt)

	private fun Task.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted task has no id")
}
