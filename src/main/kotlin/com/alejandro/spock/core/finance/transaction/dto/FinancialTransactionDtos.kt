package com.alejandro.spock.core.finance.transaction.dto

import com.alejandro.spock.core.finance.transaction.model.ExpenseCategory
import com.alejandro.spock.core.finance.transaction.model.IncomeCategory
import com.alejandro.spock.core.finance.transaction.model.TransactionDirection
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class CreateFinancialTransactionRequest(
	val accountId: UUID? = null,
	@field:NotBlank val name: String,
	val description: String? = null,
	@field:NotNull val amount: BigDecimal,
	@field:NotNull val direction: TransactionDirection,
	val incomeCategory: IncomeCategory? = null,
	val expenseCategory: ExpenseCategory? = null,
	val transactionDate: LocalDate = LocalDate.now(),
)

data class UpdateFinancialTransactionRequest(
	val name: String? = null,
	val description: String? = null,
	val amount: BigDecimal? = null,
	val incomeCategory: IncomeCategory? = null,
	val expenseCategory: ExpenseCategory? = null,
	val transactionDate: LocalDate? = null,
)

data class FinancialTransactionResponse(
	val id: UUID,
	val accountId: UUID?,
	val name: String,
	val description: String?,
	val amount: BigDecimal,
	val direction: TransactionDirection,
	val incomeCategory: IncomeCategory?,
	val expenseCategory: ExpenseCategory?,
	val transactionDate: LocalDate,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
)
