package com.alejandro.spock.core.gtd.project.service

import com.alejandro.spock.core.gtd.project.dto.CreateProjectRequest
import com.alejandro.spock.core.gtd.project.dto.ProjectResponse
import com.alejandro.spock.core.gtd.project.dto.UpdateProjectRequest
import com.alejandro.spock.core.gtd.project.model.Project
import com.alejandro.spock.core.gtd.project.repository.ProjectRepository
import com.alejandro.spock.core.shared.model.entity.BaseEntity
import com.alejandro.spock.core.shared.model.entity.EntityType
import com.alejandro.spock.core.shared.repository.BaseEntityRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class ProjectService(
	private val projectRepository: ProjectRepository,
	private val baseEntityRepository: BaseEntityRepository,
) {
	@Transactional(readOnly = true)
	fun listProjects(): List<ProjectResponse> =
		projectRepository.findAll().map { it.toResponse() }.sortedBy { it.title }

	@Transactional
	fun createProject(request: CreateProjectRequest): ProjectResponse {
		val entity = baseEntityRepository.save(
			BaseEntity(
				entityType = EntityType.PROJECT,
				title = request.title,
				summary = request.summary,
				status = request.status.name,
			),
		)
		return projectRepository.save(
			Project(entity = entity, status = request.status, startedAt = request.startedAt, dueDate = request.dueDate, endedAt = request.endedAt),
		).toResponse()
	}

	@Transactional(readOnly = true)
	fun getProject(id: UUID): ProjectResponse =
		project(id).toResponse()

	@Transactional
	fun updateProject(id: UUID, request: UpdateProjectRequest): ProjectResponse {
		val project = project(id)
		request.title?.let { project.entity.title = it }
		request.summary?.let { project.entity.summary = it }
		request.status?.let {
			project.status = it
			project.entity.status = it.name
		}
		request.startedAt?.let { project.startedAt = it }
		request.dueDate?.let { project.dueDate = it }
		request.endedAt?.let { project.endedAt = it }
		return projectRepository.save(project).toResponse()
	}

	private fun project(id: UUID): Project =
		projectRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Project $id not found") }

	private fun Project.toResponse(): ProjectResponse =
		ProjectResponse(id = requiredId(), title = entity.title, summary = entity.summary, status = status, startedAt = startedAt, dueDate = dueDate, endedAt = endedAt, createdAt = entity.createdAt, updatedAt = entity.updatedAt)

	private fun Project.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted project has no id")
}
