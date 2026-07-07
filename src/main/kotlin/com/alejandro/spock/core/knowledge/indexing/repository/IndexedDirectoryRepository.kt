package com.alejandro.spock.core.knowledge.indexing.repository

import com.alejandro.spock.core.knowledge.indexing.model.IndexedDirectory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface IndexedDirectoryRepository : JpaRepository<IndexedDirectory, UUID> {
	fun findAllByOrderByPathAsc(): List<IndexedDirectory>
}
