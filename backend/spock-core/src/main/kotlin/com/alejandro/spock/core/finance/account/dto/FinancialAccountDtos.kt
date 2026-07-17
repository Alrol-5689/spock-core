package com.alejandro.spock.core.finance.account.dto

import com.alejandro.spock.core.finance.account.model.FinancialAccountType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

data class CreateFinancialAccountRequest(
	@field:NotBlank val name: String,
	@field:NotNull val accountType: FinancialAccountType,
	val institution: String? = null,
	val currency: String = "EUR",
	val initialBalance: BigDecimal = BigDecimal.ZERO,
	val isActive: Boolean = true,
)

data class UpdateFinancialAccountRequest(
	val name: String? = null,
	val institution: String? = null,
	val isActive: Boolean? = null,
)

data class FinancialAccountResponse(
	val id: UUID,
	val name: String,
	val accountType: FinancialAccountType,
	val institution: String?,
	val currency: String,
	val initialBalance: BigDecimal,
	val isActive: Boolean,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
)
