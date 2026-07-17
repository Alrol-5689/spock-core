package com.alejandro.spock.core.knowledge.file.service

import com.alejandro.spock.core.knowledge.file.dto.CreateManagedFileRequest
import com.alejandro.spock.core.knowledge.file.dto.ManagedFileResponse
import com.alejandro.spock.core.knowledge.file.repository.ManagedFileRepository
import com.alejandro.spock.core.knowledge.file.model.ManagedFile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ManagedFileService(
	private val managedFileRepository: ManagedFileRepository,
) {
	@Transactional(readOnly = true)
	fun listFiles(): List<ManagedFileResponse> =
		managedFileRepository.findAllByOrderByFilePathAsc().map { it.toResponse() }

	@Transactional
	fun createFile(request: CreateManagedFileRequest): ManagedFileResponse =
		managedFileRepository.save(
			ManagedFile(
				filePath = request.filePath,
				displayName = request.displayName,
				originalFilename = request.originalFilename,
				fileKind = request.fileKind,
				mimeType = request.mimeType,
				sizeBytes = request.sizeBytes,
				checksumSha256 = request.checksumSha256,
				storageMode = request.storageMode,
				lastSeenAt = request.lastSeenAt,
			),
		).toResponse()

	private fun ManagedFile.toResponse(): ManagedFileResponse =
		ManagedFileResponse(id = requiredId(), filePath = filePath, displayName = displayName, originalFilename = originalFilename, fileKind = fileKind, mimeType = mimeType, sizeBytes = sizeBytes, checksumSha256 = checksumSha256, storageMode = storageMode, lastSeenAt = lastSeenAt, missingAt = missingAt, createdAt = createdAt, updatedAt = updatedAt)

	private fun ManagedFile.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted file has no id")
}
