package com.alejandro.spock.core.reminder.controller

import com.alejandro.spock.core.reminder.dto.CreateReminderRequest
import com.alejandro.spock.core.reminder.dto.ReminderResponse
import com.alejandro.spock.core.reminder.dto.UpdateReminderRequest
import com.alejandro.spock.core.reminder.model.ReminderStatus
import com.alejandro.spock.core.reminder.service.ReminderService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime
import java.util.UUID

@RestController
class ReminderController(
	private val reminderService: ReminderService,
) {
	@GetMapping("/reminders")
	fun listReminders(@RequestParam(required = false) status: ReminderStatus?): List<ReminderResponse> =
		reminderService.listReminders(status)

	@GetMapping("/reminders/due")
	fun listDueReminders(@RequestParam(required = false) until: OffsetDateTime?): List<ReminderResponse> =
		reminderService.listDueReminders(until ?: OffsetDateTime.now())

	@PostMapping("/reminders")
	@ResponseStatus(HttpStatus.CREATED)
	fun createReminder(@Valid @RequestBody request: CreateReminderRequest): ReminderResponse =
		reminderService.createReminder(request)

	@GetMapping("/reminders/{id}")
	fun getReminder(@PathVariable id: UUID): ReminderResponse =
		reminderService.getReminder(id)

	@PatchMapping("/reminders/{id}")
	fun updateReminder(@PathVariable id: UUID, @Valid @RequestBody request: UpdateReminderRequest): ReminderResponse =
		reminderService.updateReminder(id, request)
}
