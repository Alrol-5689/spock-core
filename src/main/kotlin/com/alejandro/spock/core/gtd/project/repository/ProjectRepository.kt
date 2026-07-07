package com.alejandro.spock.core.gtd.project.repository

import com.alejandro.spock.core.gtd.project.model.Project
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProjectRepository : JpaRepository<Project, UUID>
