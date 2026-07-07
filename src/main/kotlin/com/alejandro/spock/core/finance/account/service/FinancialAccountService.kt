package com.alejandro.spock.core.finance.account.service

import com.alejandro.spock.core.finance.account.dto.CreateFinancialAccountRequest
import com.alejandro.spock.core.finance.account.dto.FinancialAccountResponse
import com.alejandro.spock.core.finance.account.dto.UpdateFinancialAccountRequest
import com.alejandro.spock.core.finance.account.model.FinancialAccount
import com.alejandro.spock.core.finance.account.repository.FinancialAccountRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class FinancialAccountService(
	private val accountRepository: FinancialAccountRepository,
) {
	@Transactional(readOnly = true)
	fun listAccounts(): List<FinancialAccountResponse> =
		accountRepository.findAllByOrderByNameAsc().map { it.toResponse() }

	@Transactional
	fun createAccount(request: CreateFinancialAccountRequest): FinancialAccountResponse =
		accountRepository.save(
			FinancialAccount(
				name = request.name,
				accountType = request.accountType,
				institution = request.institution,
				currency = request.currency,
				initialBalance = request.initialBalance,
				isActive = request.isActive,
			),
		).toResponse()

	@Transactional
	fun updateAccount(id: UUID, request: UpdateFinancialAccountRequest): FinancialAccountResponse {
		val account = accountRepository.findById(id).orElseThrow {
			ResponseStatusException(HttpStatus.NOT_FOUND, "Financial account $id not found")
		}
		request.name?.let { account.name = it }
		request.institution?.let { account.institution = it }
		request.isActive?.let { account.isActive = it }
		return accountRepository.save(account).toResponse()
	}

	private fun FinancialAccount.toResponse(): FinancialAccountResponse =
		FinancialAccountResponse(id = requiredId(), name = name, accountType = accountType, institution = institution, currency = currency, initialBalance = initialBalance, isActive = isActive, createdAt = createdAt, updatedAt = updatedAt)

	private fun FinancialAccount.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted account has no id")
}
