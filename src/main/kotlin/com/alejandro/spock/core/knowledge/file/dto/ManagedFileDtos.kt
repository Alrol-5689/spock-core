package com.alejandro.spock.core.knowledge.file.dto

import com.alejandro.spock.core.knowledge.file.model.FileKind
import com.alejandro.spock.core.knowledge.file.model.FileStorageMode
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.util.UUID

data class CreateManagedFileRequest(
	@field:NotBlank val filePath: String,
	val displayName: String? = null,
	val originalFilename: String? = null,
	@field:NotNull val fileKind: FileKind,
	val mimeType: String? = null,
	val sizeBytes: Long? = null,
	val checksumSha256: String? = null,
	val storageMode: FileStorageMode = FileStorageMode.REFERENCED,
	val lastSeenAt: OffsetDateTime? = null,
)

data class ManagedFileResponse(
	val id: UUID,
	val filePath: String,
	val displayName: String?,
	val originalFilename: String?,
	val fileKind: FileKind,
	val mimeType: String?,
	val sizeBytes: Long?,
	val checksumSha256: String?,
	val storageMode: FileStorageMode,
	val lastSeenAt: OffsetDateTime?,
	val missingAt: OffsetDateTime?,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
)
