package com.alejandro.spock.core.gtd.project.controller

import com.alejandro.spock.core.gtd.project.dto.CreateProjectRequest
import com.alejandro.spock.core.gtd.project.dto.ProjectResponse
import com.alejandro.spock.core.gtd.project.dto.UpdateProjectRequest
import com.alejandro.spock.core.gtd.project.service.ProjectService
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
class ProjectController(
	private val projectService: ProjectService,
) {
	@GetMapping("/projects")
	fun listProjects(): List<ProjectResponse> =
		projectService.listProjects()

	@GetMapping("/projects/open")
	fun listOpenProjects(): List<ProjectResponse> =
		projectService.listOpenProjects()

	@PostMapping("/projects")
	@ResponseStatus(HttpStatus.CREATED)
	fun createProject(@Valid @RequestBody request: CreateProjectRequest): ProjectResponse =
		projectService.createProject(request)

	@GetMapping("/projects/{id}")
	fun getProject(@PathVariable id: UUID): ProjectResponse =
		projectService.getProject(id)

	@PatchMapping("/projects/{id}")
	fun updateProject(@PathVariable id: UUID, @Valid @RequestBody request: UpdateProjectRequest): ProjectResponse =
		projectService.updateProject(id, request)
}
