package com.alejandro.spock.core.gtd.event.controller

import com.alejandro.spock.core.gtd.event.dto.CreateEventRequest
import com.alejandro.spock.core.gtd.event.dto.EventResponse
import com.alejandro.spock.core.gtd.event.dto.UpdateEventRequest
import com.alejandro.spock.core.gtd.event.service.EventService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class EventController(
	private val eventService: EventService,
) {
	@GetMapping("/events")
	fun listEvents(): List<EventResponse> =
		eventService.listEvents()

	@PostMapping("/events")
	@ResponseStatus(HttpStatus.CREATED)
	fun createEvent(@Valid @RequestBody request: CreateEventRequest): EventResponse =
		eventService.createEvent(request)

	@PatchMapping("/events/{id}")
	fun updateEvent(@PathVariable id: UUID, @Valid @RequestBody request: UpdateEventRequest): EventResponse =
		eventService.updateEvent(id, request)
}
