package com.alejandro.spock.core.gtd.tag.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.util.UUID

data class CreateTagRequest(
	@field:NotBlank val name: String,
)

data class TagResponse(
	val id: UUID,
	val name: String,
	val createdAt: OffsetDateTime?,
)

data class ApplyTagRequest(
	@field:NotNull val entityId: UUID,
	@field:NotNull val tagId: UUID,
)

data class EntityTagResponse(
	val id: UUID,
	val entityId: UUID,
	val tagId: UUID,
	val tagName: String,
	val createdAt: OffsetDateTime?,
)
