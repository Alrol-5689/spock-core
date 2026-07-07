package com.alejandro.spock.core.gtd.area.dto

import com.alejandro.spock.core.gtd.area.model.AreaStatus
import com.alejandro.spock.core.gtd.area.model.AreaType
import jakarta.validation.constraints.NotBlank
import java.time.OffsetDateTime
import java.util.UUID

data class CreateAreaRequest(
	@field:NotBlank val title: String,
	val summary: String? = null,
	val type: AreaType = AreaType.PERSONAL,
	val status: AreaStatus = AreaStatus.ACTIVE,
)

data class UpdateAreaRequest(
	val title: String? = null,
	val summary: String? = null,
	val type: AreaType? = null,
	val status: AreaStatus? = null,
)

data class AreaResponse(
	val id: UUID,
	val title: String,
	val summary: String?,
	val type: AreaType,
	val status: AreaStatus,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
)
