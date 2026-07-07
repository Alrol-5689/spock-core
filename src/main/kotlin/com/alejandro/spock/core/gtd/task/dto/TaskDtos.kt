package com.alejandro.spock.core.gtd.task.dto

import com.alejandro.spock.core.gtd.task.model.TaskPriority
import com.alejandro.spock.core.gtd.task.model.TaskStatus
import jakarta.validation.constraints.NotBlank
import java.time.OffsetDateTime
import java.util.UUID

data class CreateTaskRequest(
	@field:NotBlank val title: String,
	val summary: String? = null,
	val status: TaskStatus = TaskStatus.OPEN,
	val priority: TaskPriority? = null,
	val dueAt: OffsetDateTime? = null,
	val scheduledAt: OffsetDateTime? = null,
)

data class UpdateTaskRequest(
	val title: String? = null,
	val summary: String? = null,
	val status: TaskStatus? = null,
	val priority: TaskPriority? = null,
	val dueAt: OffsetDateTime? = null,
	val scheduledAt: OffsetDateTime? = null,
	val completedAt: OffsetDateTime? = null,
)

data class TaskResponse(
	val id: UUID,
	val title: String,
	val summary: String?,
	val status: TaskStatus,
	val priority: TaskPriority?,
	val dueAt: OffsetDateTime?,
	val scheduledAt: OffsetDateTime?,
	val completedAt: OffsetDateTime?,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
)
