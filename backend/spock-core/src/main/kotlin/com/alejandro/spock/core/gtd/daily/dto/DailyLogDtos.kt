package com.alejandro.spock.core.gtd.daily.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class CreateDailyLogRequest(
	@field:NotNull
	val logDate: LocalDate,
	val title: String? = null,
	val summary: String? = null,
	val markdownPath: String? = null,
)

data class UpdateDailyLogRequest(
	val title: String? = null,
	val summary: String? = null,
	val markdownPath: String? = null,
)

data class DailyLogResponse(
	val id: UUID,
	val logDate: LocalDate,
	val title: String,
	val summary: String?,
	val pageId: UUID?,
	val markdownPath: String?,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
)
