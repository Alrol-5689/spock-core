package com.alejandro.spock.core.finance.transaction.controller

import com.alejandro.spock.core.finance.transaction.dto.CreateFinancialTransactionRequest
import com.alejandro.spock.core.finance.transaction.dto.FinancialTransactionResponse
import com.alejandro.spock.core.finance.transaction.dto.UpdateFinancialTransactionRequest
import com.alejandro.spock.core.finance.transaction.service.FinancialTransactionService
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
class FinancialTransactionController(
	private val transactionService: FinancialTransactionService,
) {
	@GetMapping("/finance/transactions")
	fun listTransactions(
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate?,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate?,
	): List<FinancialTransactionResponse> =
		transactionService.listTransactions(from, to)

	@PostMapping("/finance/transactions")
	@ResponseStatus(HttpStatus.CREATED)
	fun createTransaction(@Valid @RequestBody request: CreateFinancialTransactionRequest): FinancialTransactionResponse =
		transactionService.createTransaction(request)

	@PatchMapping("/finance/transactions/{id}")
	fun updateTransaction(
		@PathVariable id: UUID,
		@Valid @RequestBody request: UpdateFinancialTransactionRequest,
	): FinancialTransactionResponse =
		transactionService.updateTransaction(id, request)
}
