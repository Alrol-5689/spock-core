package com.alejandro.spock.core.knowledge.page.service

import com.alejandro.spock.core.knowledge.file.repository.ManagedFileRepository
import com.alejandro.spock.core.knowledge.file.model.ManagedFile
import com.alejandro.spock.core.knowledge.page.model.Page
import com.alejandro.spock.core.knowledge.page.model.PageFile
import com.alejandro.spock.core.knowledge.page.dto.AttachFileToPageRequest
import com.alejandro.spock.core.knowledge.page.dto.CreatePageRequest
import com.alejandro.spock.core.knowledge.page.dto.PageFileResponse
import com.alejandro.spock.core.knowledge.page.dto.PageResponse
import com.alejandro.spock.core.knowledge.page.dto.UpdatePageRequest
import com.alejandro.spock.core.knowledge.page.repository.PageFileRepository
import com.alejandro.spock.core.knowledge.page.repository.PageRepository
import com.alejandro.spock.core.shared.model.entity.BaseEntity
import com.alejandro.spock.core.shared.repository.BaseEntityRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class PageService(
	private val baseEntityRepository: BaseEntityRepository,
	private val pageRepository: PageRepository,
	private val managedFileRepository: ManagedFileRepository,
	private val pageFileRepository: PageFileRepository,
) {
	@Transactional(readOnly = true)
	fun listPages(): List<PageResponse> =
		pageRepository.findAll().map { it.toResponse() }.sortedBy { it.markdownPath }

	@Transactional
	fun createPage(request: CreatePageRequest): PageResponse {
		val entity = entity(request.entityId)
		return pageRepository.save(Page(entity = entity, title = request.title, markdownPath = request.markdownPath)).toResponse()
	}

	@Transactional(readOnly = true)
	fun getPage(id: UUID): PageResponse =
		page(id).toResponse()

	@Transactional(readOnly = true)
	fun getPageByEntity(entityId: UUID): PageResponse =
		(pageRepository.findByEntityId(entityId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Page for entity $entityId not found")).toResponse()

	@Transactional
	fun updatePage(id: UUID, request: UpdatePageRequest): PageResponse {
		val page = page(id)
		request.title?.let { page.title = it }
		request.markdownPath?.let { page.markdownPath = it }
		request.lastSyncedAt?.let { page.lastSyncedAt = it }
		return pageRepository.save(page).toResponse()
	}

	@Transactional
	fun attachFileToPage(request: AttachFileToPageRequest): PageFileResponse {
		val page = page(request.pageId)
		val file = file(request.fileId)
		return pageFileRepository.save(PageFile(page = page, file = file)).toResponse()
	}

	@Transactional(readOnly = true)
	fun listPageFiles(pageId: UUID): List<PageFileResponse> {
		page(pageId)
		return pageFileRepository.findAllByPageId(pageId).map { it.toResponse() }
	}

	private fun entity(id: UUID): BaseEntity =
		baseEntityRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Entity $id not found") }

	private fun page(id: UUID): Page =
		pageRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Page $id not found") }

	private fun file(id: UUID): ManagedFile =
		managedFileRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "File $id not found") }

	private fun Page.toResponse(): PageResponse =
		PageResponse(id = requiredId(), entityId = entity.requiredId(), title = title, markdownPath = markdownPath, createdAt = createdAt, updatedAt = updatedAt, lastSyncedAt = lastSyncedAt)

	private fun PageFile.toResponse(): PageFileResponse =
		PageFileResponse(id = requiredId(), pageId = page.requiredId(), fileId = file.requiredId(), createdAt = createdAt)

	private fun Page.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted page has no id")

	private fun PageFile.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted page file has no id")

	private fun ManagedFile.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted file has no id")

	private fun BaseEntity.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted entity has no id")
}
