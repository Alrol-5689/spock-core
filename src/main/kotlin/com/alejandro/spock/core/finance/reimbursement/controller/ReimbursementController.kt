package com.alejandro.spock.core.finance.reimbursement.controller

import com.alejandro.spock.core.finance.reimbursement.dto.CreateReimbursementRequest
import com.alejandro.spock.core.finance.reimbursement.dto.ReimbursementResponse
import com.alejandro.spock.core.finance.reimbursement.service.ReimbursementService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ReimbursementController(
	private val reimbursementService: ReimbursementService,
) {
	@PostMapping("/finance/reimbursements")
	@ResponseStatus(HttpStatus.CREATED)
	fun createReimbursement(@Valid @RequestBody request: CreateReimbursementRequest): ReimbursementResponse =
		reimbursementService.createReimbursement(request)
}
