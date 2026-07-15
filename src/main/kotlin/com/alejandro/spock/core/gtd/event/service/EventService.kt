package com.alejandro.spock.core.gtd.event.service

import com.alejandro.spock.core.gtd.event.dto.CreateEventRequest
import com.alejandro.spock.core.gtd.event.dto.EventResponse
import com.alejandro.spock.core.gtd.event.dto.UpdateEventRequest
import com.alejandro.spock.core.gtd.event.model.Event
import com.alejandro.spock.core.gtd.event.repository.EventRepository
import com.alejandro.spock.core.shared.model.entity.BaseEntity
import com.alejandro.spock.core.shared.model.entity.EntityType
import com.alejandro.spock.core.shared.repository.BaseEntityRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class EventService(
	private val eventRepository: EventRepository,
	private val baseEntityRepository: BaseEntityRepository,
) {
	@Transactional(readOnly = true)
	fun listEvents(): List<EventResponse> =
		eventRepository.findAll().map { it.toResponse() }.sortedBy { it.startsAt }

	@Transactional(readOnly = true)
	fun listEventsForDate(date: LocalDate = LocalDate.now(ZoneOffset.UTC)): List<EventResponse> {
		val from = date.atStartOfDay().atOffset(ZoneOffset.UTC)
		val to = date.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC)
		return eventRepository.findAllByStartsAtBetweenOrderByStartsAtAsc(from, to).map { it.toResponse() }
	}

	@Transactional
	fun createEvent(request: CreateEventRequest): EventResponse {
		val entity = baseEntityRepository.save(BaseEntity(
            entityType = EntityType.EVENT,
            title = request.title,
            summary = request.summary
        ))
		return eventRepository.save(
			Event(
				entity = entity,
				startsAt = request.startsAt,
				endsAt = request.endsAt,
				eventType = request.eventType,
				allDay = request.allDay,
				location = request.location,
			),
		).toResponse()
	}

	@Transactional
	fun updateEvent(id: UUID, request: UpdateEventRequest): EventResponse {
		val event = event(id)
		request.title?.let { event.entity.title = it }
		request.summary?.let { event.entity.summary = it }
		request.startsAt?.let { event.startsAt = it }
		request.endsAt?.let { event.endsAt = it }
		request.eventType?.let { event.eventType = it }
		request.allDay?.let { event.allDay = it }
		request.location?.let { event.location = it }
		request.archived?.let { archived ->
			event.entity.archivedAt = if (archived) OffsetDateTime.now() else null
		}
		return eventRepository.save(event).toResponse()
	}

	private fun event(id: UUID): Event =
		eventRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Event $id not found") }

	private fun Event.toResponse(): EventResponse =
		EventResponse(
			id = requiredId(),
			title = entity.title,
			summary = entity.summary,
			startsAt = startsAt,
			endsAt = endsAt,
			eventType = eventType,
			allDay = allDay,
			location = location,
			archivedAt = entity.archivedAt,
			createdAt = entity.createdAt,
			updatedAt = entity.updatedAt,
		)

	private fun Event.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted event has no id")
}
