package com.alejandro.spock.core.gtd.habit.controller

import com.alejandro.spock.core.gtd.habit.dto.CreateHabitOccurrenceRequest
import com.alejandro.spock.core.gtd.habit.dto.CreateHabitRequest
import com.alejandro.spock.core.gtd.habit.dto.CreateHabitVersionRequest
import com.alejandro.spock.core.gtd.habit.dto.HabitOccurrenceResponse
import com.alejandro.spock.core.gtd.habit.dto.HabitResponse
import com.alejandro.spock.core.gtd.habit.dto.HabitVersionResponse
import com.alejandro.spock.core.gtd.habit.dto.UpdateHabitOccurrenceRequest
import com.alejandro.spock.core.gtd.habit.dto.UpdateHabitRequest
import com.alejandro.spock.core.gtd.habit.service.HabitService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping
class HabitController(
	private val habitService: HabitService,
) {
	@GetMapping("/habits")
	fun listHabits(): List<HabitResponse> =
		habitService.listHabits()

	@PostMapping("/habits")
	@ResponseStatus(HttpStatus.CREATED)
	fun createHabit(@Valid @RequestBody request: CreateHabitRequest): HabitResponse =
		habitService.createHabit(request)

	@GetMapping("/habits/{habitId}")
	fun getHabit(@PathVariable habitId: UUID): HabitResponse =
		habitService.getHabit(habitId)

	@PatchMapping("/habits/{habitId}")
	fun updateHabit(
		@PathVariable habitId: UUID,
		@Valid @RequestBody request: UpdateHabitRequest,
	): HabitResponse =
		habitService.updateHabit(habitId, request)

	@GetMapping("/habits/{habitId}/versions")
	fun listVersions(@PathVariable habitId: UUID): List<HabitVersionResponse> =
		habitService.listVersions(habitId)

	@PostMapping("/habits/{habitId}/versions")
	@ResponseStatus(HttpStatus.CREATED)
	fun createVersion(
		@PathVariable habitId: UUID,
		@Valid @RequestBody request: CreateHabitVersionRequest,
	): HabitVersionResponse =
		habitService.createVersion(habitId, request)

	@GetMapping("/habits/{habitId}/occurrences")
	fun listOccurrences(
		@PathVariable habitId: UUID,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate,
	): List<HabitOccurrenceResponse> =
		habitService.listOccurrences(habitId, from, to)

	@PostMapping("/habits/{habitId}/occurrences")
	@ResponseStatus(HttpStatus.CREATED)
	fun createOccurrence(
		@PathVariable habitId: UUID,
		@Valid @RequestBody request: CreateHabitOccurrenceRequest,
	): HabitOccurrenceResponse =
		habitService.createOccurrence(habitId, request)

	@PatchMapping("/habit-occurrences/{occurrenceId}")
	fun updateOccurrence(
		@PathVariable occurrenceId: UUID,
		@Valid @RequestBody request: UpdateHabitOccurrenceRequest,
	): HabitOccurrenceResponse =
		habitService.updateOccurrence(occurrenceId, request)
}
