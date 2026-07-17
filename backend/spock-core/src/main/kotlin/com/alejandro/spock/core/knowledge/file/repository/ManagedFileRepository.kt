package com.alejandro.spock.core.knowledge.file.repository

import com.alejandro.spock.core.knowledge.file.model.ManagedFile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ManagedFileRepository : JpaRepository<ManagedFile, UUID> {
	fun findAllByOrderByFilePathAsc(): List<ManagedFile>
}
