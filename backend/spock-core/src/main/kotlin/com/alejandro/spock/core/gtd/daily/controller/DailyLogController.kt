package com.alejandro.spock.core.gtd.daily.controller

import com.alejandro.spock.core.gtd.daily.dto.CreateDailyLogRequest
import com.alejandro.spock.core.gtd.daily.dto.DailyLogResponse
import com.alejandro.spock.core.gtd.daily.dto.UpdateDailyLogRequest
import com.alejandro.spock.core.gtd.daily.service.DailyLogService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

@RestController
class DailyLogController(
	private val dailyLogService: DailyLogService,
) {
	@GetMapping("/daily-logs")
	fun listDailyLogs(
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate?,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate?,
	): List<DailyLogResponse> =
		dailyLogService.listDailyLogs(from, to)

	@PostMapping("/daily-logs")
	@ResponseStatus(HttpStatus.CREATED)
	fun createDailyLog(@Valid @RequestBody request: CreateDailyLogRequest): DailyLogResponse =
		dailyLogService.createDailyLog(request)

	@GetMapping("/daily-logs/{id}")
	fun getDailyLog(@PathVariable id: UUID): DailyLogResponse =
		dailyLogService.getDailyLog(id)

	@GetMapping("/daily-logs/by-date/{logDate}")
	fun getDailyLogByDate(
		@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) logDate: LocalDate,
	): DailyLogResponse =
		dailyLogService.getDailyLogByDate(logDate)

	@PatchMapping("/daily-logs/{id}")
	fun updateDailyLog(
		@PathVariable id: UUID,
		@Valid @RequestBody request: UpdateDailyLogRequest,
	): DailyLogResponse =
		dailyLogService.updateDailyLog(id, request)
}
