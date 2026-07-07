package com.alejandro.spock.core.finance.reimbursement.dto

import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class CreateReimbursementRequest(
	@field:NotNull val transactionId: UUID,
	@field:NotNull val amount: BigDecimal,
	val payerName: String? = null,
	val note: String? = null,
	val reimbursementDate: LocalDate = LocalDate.now(),
)

data class ReimbursementResponse(
	val id: UUID,
	val transactionId: UUID,
	val amount: BigDecimal,
	val payerName: String?,
	val note: String?,
	val reimbursementDate: LocalDate,
)
