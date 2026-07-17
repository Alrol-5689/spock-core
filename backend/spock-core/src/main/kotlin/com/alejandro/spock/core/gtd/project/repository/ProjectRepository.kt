package com.alejandro.spock.core.gtd.project.repository

import com.alejandro.spock.core.gtd.project.model.Project
import com.alejandro.spock.core.gtd.project.model.ProjectStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProjectRepository : JpaRepository<Project, UUID> {
	fun findAllByStatusIn(statuses: Collection<ProjectStatus>): List<Project>
}
