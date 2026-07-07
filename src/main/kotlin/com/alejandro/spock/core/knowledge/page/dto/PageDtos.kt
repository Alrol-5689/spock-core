package com.alejandro.spock.core.knowledge.page.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.util.UUID

data class CreatePageRequest(
	@field:NotNull val entityId: UUID,
	@field:NotBlank val title: String,
	@field:NotBlank val markdownPath: String,
)

data class UpdatePageRequest(
	val title: String? = null,
	val markdownPath: String? = null,
	val lastSyncedAt: OffsetDateTime? = null,
)

data class PageResponse(
	val id: UUID,
	val entityId: UUID,
	val title: String,
	val markdownPath: String,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
	val lastSyncedAt: OffsetDateTime?,
)

data class AttachFileToPageRequest(
	@field:NotNull val pageId: UUID,
	@field:NotNull val fileId: UUID,
)

data class PageFileResponse(
	val id: UUID,
	val pageId: UUID,
	val fileId: UUID,
	val createdAt: OffsetDateTime?,
)
