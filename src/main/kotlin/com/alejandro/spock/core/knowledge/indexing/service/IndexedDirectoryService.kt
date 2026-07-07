package com.alejandro.spock.core.knowledge.indexing.service

import com.alejandro.spock.core.knowledge.indexing.dto.CreateIndexedDirectoryRequest
import com.alejandro.spock.core.knowledge.indexing.dto.IndexedDirectoryResponse
import com.alejandro.spock.core.knowledge.indexing.dto.UpdateIndexedDirectoryRequest
import com.alejandro.spock.core.knowledge.indexing.repository.IndexedDirectoryRepository
import com.alejandro.spock.core.knowledge.indexing.model.IndexedDirectory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class IndexedDirectoryService(
	private val indexedDirectoryRepository: IndexedDirectoryRepository,
) {
	@Transactional(readOnly = true)
	fun listIndexedDirectories(): List<IndexedDirectoryResponse> =
		indexedDirectoryRepository.findAllByOrderByPathAsc().map { it.toResponse() }

	@Transactional
	fun createIndexedDirectory(request: CreateIndexedDirectoryRequest): IndexedDirectoryResponse =
		indexedDirectoryRepository.save(
			IndexedDirectory(path = request.path, enabled = request.enabled, recursive = request.recursive),
		).toResponse()

	@Transactional
	fun updateIndexedDirectory(id: UUID, request: UpdateIndexedDirectoryRequest): IndexedDirectoryResponse {
		val directory = indexedDirectoryRepository.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Indexed directory $id not found")
		}
		request.enabled?.let { directory.enabled = it }
		request.recursive?.let { directory.recursive = it }
		request.lastScannedAt?.let { directory.lastScannedAt = it }
		return indexedDirectoryRepository.save(directory).toResponse()
	}

	private fun IndexedDirectory.toResponse(): IndexedDirectoryResponse =
		IndexedDirectoryResponse(id = requiredId(), path = path, enabled = enabled, recursive = recursive, lastScannedAt = lastScannedAt, createdAt = createdAt, updatedAt = updatedAt)

	private fun IndexedDirectory.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted indexed directory has no id")
}
