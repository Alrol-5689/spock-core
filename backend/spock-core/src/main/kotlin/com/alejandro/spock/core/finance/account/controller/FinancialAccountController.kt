package com.alejandro.spock.core.finance.account.controller

import com.alejandro.spock.core.finance.account.dto.CreateFinancialAccountRequest
import com.alejandro.spock.core.finance.account.dto.FinancialAccountResponse
import com.alejandro.spock.core.finance.account.dto.UpdateFinancialAccountRequest
import com.alejandro.spock.core.finance.account.service.FinancialAccountService
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
class FinancialAccountController(
	private val accountService: FinancialAccountService,
) {
	@GetMapping("/finance/accounts")
	fun listAccounts(): List<FinancialAccountResponse> =
		accountService.listAccounts()

	@PostMapping("/finance/accounts")
	@ResponseStatus(HttpStatus.CREATED)
	fun createAccount(@Valid @RequestBody request: CreateFinancialAccountRequest): FinancialAccountResponse =
		accountService.createAccount(request)

	@PatchMapping("/finance/accounts/{id}")
	fun updateAccount(
		@PathVariable id: UUID,
		@Valid @RequestBody request: UpdateFinancialAccountRequest,
	): FinancialAccountResponse =
		accountService.updateAccount(id, request)
}
