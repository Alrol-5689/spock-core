package com.alejandro.spock.core.finance.transaction.service

import com.alejandro.spock.core.finance.account.model.FinancialAccount
import com.alejandro.spock.core.finance.account.repository.FinancialAccountRepository
import com.alejandro.spock.core.finance.transaction.dto.CreateFinancialTransactionRequest
import com.alejandro.spock.core.finance.transaction.dto.FinancialTransactionResponse
import com.alejandro.spock.core.finance.transaction.dto.UpdateFinancialTransactionRequest
import com.alejandro.spock.core.finance.transaction.model.FinancialTransaction
import com.alejandro.spock.core.finance.transaction.model.TransactionDirection
import com.alejandro.spock.core.finance.transaction.repository.FinancialTransactionRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.util.UUID

@Service
class FinancialTransactionService(
	private val transactionRepository: FinancialTransactionRepository,
	private val accountRepository: FinancialAccountRepository,
) {
	@Transactional(readOnly = true)
	fun listTransactions(from: LocalDate?, to: LocalDate?): List<FinancialTransactionResponse> =
		if (from != null && to != null) {
			transactionRepository.findAllByTransactionDateBetweenOrderByTransactionDateDesc(from, to)
		} else {
			transactionRepository.findAll().sortedByDescending { it.transactionDate }
		}.map { it.toResponse() }

	@Transactional
	fun createTransaction(request: CreateFinancialTransactionRequest): FinancialTransactionResponse {
		validateTransactionCategories(request.direction, request.incomeCategory, request.expenseCategory)
		val account = request.accountId?.let { account(it) }
		return transactionRepository.save(
			FinancialTransaction(
				account = account,
				name = request.name,
				description = request.description,
				amount = request.amount,
				direction = request.direction,
				incomeCategory = request.incomeCategory,
				expenseCategory = request.expenseCategory,
				transactionDate = request.transactionDate,
			),
		).toResponse()
	}

	@Transactional
	fun updateTransaction(id: UUID, request: UpdateFinancialTransactionRequest): FinancialTransactionResponse {
		val transaction = transaction(id)
		request.name?.let { transaction.name = it }
		request.description?.let { transaction.description = it }
		request.amount?.let { transaction.amount = it }
		request.incomeCategory?.let { transaction.incomeCategory = it }
		request.expenseCategory?.let { transaction.expenseCategory = it }
		request.transactionDate?.let { transaction.transactionDate = it }
		validateTransactionCategories(transaction.direction, transaction.incomeCategory, transaction.expenseCategory)
		return transactionRepository.save(transaction).toResponse()
	}

	private fun validateTransactionCategories(direction: TransactionDirection, incomeCategory: Any?, expenseCategory: Any?) {
		if (direction == TransactionDirection.INCOME && expenseCategory != null) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "income transactions cannot have an expenseCategory")
		}
		if (direction == TransactionDirection.EXPENSE && incomeCategory != null) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "expense transactions cannot have an incomeCategory")
		}
	}

	private fun account(id: UUID): FinancialAccount =
		accountRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Financial account $id not found") }

	private fun transaction(id: UUID): FinancialTransaction =
		transactionRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Financial transaction $id not found") }

	private fun FinancialTransaction.toResponse(): FinancialTransactionResponse =
		FinancialTransactionResponse(id = requiredId(), accountId = account?.id, name = name, description = description, amount = amount, direction = direction, incomeCategory = incomeCategory, expenseCategory = expenseCategory, transactionDate = transactionDate, createdAt = createdAt, updatedAt = updatedAt)

	private fun FinancialTransaction.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted transaction has no id")
}
