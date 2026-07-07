package com.alejandro.spock.core.finance.transaction.repository

import com.alejandro.spock.core.finance.transaction.model.FinancialTransaction
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface FinancialTransactionRepository : JpaRepository<FinancialTransaction, UUID> {
	fun findAllByTransactionDateBetweenOrderByTransactionDateDesc(
		from: LocalDate,
		to: LocalDate,
	): List<FinancialTransaction>
}
