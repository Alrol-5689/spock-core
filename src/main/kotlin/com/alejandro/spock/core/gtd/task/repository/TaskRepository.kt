package com.alejandro.spock.core.gtd.task.repository

import com.alejandro.spock.core.gtd.task.model.Task
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TaskRepository : JpaRepository<Task, UUID>
