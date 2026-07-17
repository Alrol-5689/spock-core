package com.alejandro.spock.core.gtd.project.dto

import com.alejandro.spock.core.gtd.project.model.ProjectStatus
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class CreateProjectRequest(
	@field:NotBlank val title: String,
	val summary: String? = null,
	val status: ProjectStatus = ProjectStatus.ACTIVE,
	val startedAt: LocalDate? = null,
	val dueDate: LocalDate? = null,
	val endedAt: LocalDate? = null,
)

data class UpdateProjectRequest(
	val title: String? = null,
	val summary: String? = null,
	val status: ProjectStatus? = null,
	val startedAt: LocalDate? = null,
	val dueDate: LocalDate? = null,
	val endedAt: LocalDate? = null,
)

data class ProjectResponse(
	val id: UUID,
	val title: String,
	val summary: String?,
	val status: ProjectStatus,
	val startedAt: LocalDate?,
	val dueDate: LocalDate?,
	val endedAt: LocalDate?,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
)
