package com.alejandro.spock.core.gtd.event.dto

import com.alejandro.spock.core.gtd.event.model.EventType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.util.UUID

data class CreateEventRequest(
	@field:NotBlank val title: String,
	val summary: String? = null,
	@field:NotNull val startsAt: OffsetDateTime,
	val endsAt: OffsetDateTime? = null,
	val eventType: EventType = EventType.OTHER,
	val allDay: Boolean = false,
	val location: String? = null,
)

data class UpdateEventRequest(
	val title: String? = null,
	val summary: String? = null,
	val startsAt: OffsetDateTime? = null,
	val endsAt: OffsetDateTime? = null,
	val eventType: EventType? = null,
	val allDay: Boolean? = null,
	val location: String? = null,
	val archived: Boolean? = null,
)

data class EventResponse(
	val id: UUID,
	val title: String,
	val summary: String?,
	val startsAt: OffsetDateTime,
	val endsAt: OffsetDateTime?,
	val eventType: EventType,
	val allDay: Boolean,
	val location: String?,
	val archivedAt: OffsetDateTime?,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
)
