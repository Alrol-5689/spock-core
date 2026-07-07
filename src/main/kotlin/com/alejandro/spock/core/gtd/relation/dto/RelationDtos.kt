package com.alejandro.spock.core.gtd.relation.dto

import com.alejandro.spock.core.gtd.relation.model.RelationType
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.util.UUID

data class CreateRelationRequest(
	@field:NotNull val sourceEntityId: UUID,
	@field:NotNull val targetEntityId: UUID,
	@field:NotNull val relationType: RelationType,
)

data class RelationResponse(
	val id: UUID,
	val sourceEntityId: UUID,
	val targetEntityId: UUID,
	val relationType: RelationType,
	val createdAt: OffsetDateTime?,
)
