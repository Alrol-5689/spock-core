package com.alejandro.spock.core.knowledge.indexing.dto

import jakarta.validation.constraints.NotBlank
import java.time.OffsetDateTime
import java.util.UUID

data class CreateIndexedDirectoryRequest(
	@field:NotBlank val path: String,
	val enabled: Boolean = true,
	val recursive: Boolean = true,
)

data class UpdateIndexedDirectoryRequest(
	val enabled: Boolean? = null,
	val recursive: Boolean? = null,
	val lastScannedAt: OffsetDateTime? = null,
)

data class IndexedDirectoryResponse(
	val id: UUID,
	val path: String,
	val enabled: Boolean,
	val recursive: Boolean,
	val lastScannedAt: OffsetDateTime?,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
)
