package com.alejandro.spock.core.knowledge.page.repository

import com.alejandro.spock.core.knowledge.page.model.Page
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PageRepository : JpaRepository<Page, UUID> {
	fun findByEntityId(entityId: UUID): Page?

	fun findByMarkdownPath(markdownPath: String): Page?
}
