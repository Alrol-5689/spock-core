package com.alejandro.spock.core.finance.capital.controller

import com.alejandro.spock.core.finance.capital.dto.CreateMonthlyCapitalSnapshotRequest
import com.alejandro.spock.core.finance.capital.dto.MonthlyCapitalSnapshotResponse
import com.alejandro.spock.core.finance.capital.service.MonthlyCapitalSnapshotService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class MonthlyCapitalSnapshotController(
	private val snapshotService: MonthlyCapitalSnapshotService,
) {
	@GetMapping("/finance/capital-snapshots")
	fun listSnapshots(): List<MonthlyCapitalSnapshotResponse> =
		snapshotService.listSnapshots()

	@PostMapping("/finance/capital-snapshots")
	@ResponseStatus(HttpStatus.CREATED)
	fun createSnapshot(
		@Valid @RequestBody request: CreateMonthlyCapitalSnapshotRequest,
	): MonthlyCapitalSnapshotResponse =
		snapshotService.createSnapshot(request)
}
