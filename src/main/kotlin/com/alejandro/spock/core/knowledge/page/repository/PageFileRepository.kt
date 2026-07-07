package com.alejandro.spock.core.knowledge.page.repository

import com.alejandro.spock.core.knowledge.page.model.PageFile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PageFileRepository : JpaRepository<PageFile, UUID> {
	fun findAllByPageId(pageId: UUID): List<PageFile>
}
