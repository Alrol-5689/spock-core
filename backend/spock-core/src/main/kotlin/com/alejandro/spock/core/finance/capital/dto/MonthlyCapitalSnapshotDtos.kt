package com.alejandro.spock.core.finance.capital.dto

import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.YearMonth
import java.util.UUID

data class CreateMonthlyCapitalSnapshotRequest(
	@field:NotNull val month: YearMonth,
	val savingsAccount: BigDecimal = BigDecimal.ZERO,
	val piggyBank: BigDecimal = BigDecimal.ZERO,
	val checkingAccount: BigDecimal = BigDecimal.ZERO,
	val cash: BigDecimal = BigDecimal.ZERO,
)

data class MonthlyCapitalSnapshotResponse(
	val id: UUID,
	val version: Long,
	val month: YearMonth,
	val savingsAccount: BigDecimal,
	val piggyBank: BigDecimal,
	val checkingAccount: BigDecimal,
	val cash: BigDecimal,
	val totalAssets: BigDecimal,
)
