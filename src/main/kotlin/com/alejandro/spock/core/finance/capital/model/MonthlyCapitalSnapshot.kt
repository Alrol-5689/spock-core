package com.alejandro.spock.core.finance.capital.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.math.BigDecimal
import java.time.YearMonth
import java.util.UUID

@Entity
@Table(name = "finance_monthly_capital_snapshots")
class MonthlyCapitalSnapshot(
	// Identificador unico del snapshot mensual.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Version usada por JPA para control de concurrencia optimista.
	@Version
	@Column(name = "version", nullable = false)
	var version: Long = 0,

	// Mes al que corresponde el snapshot.
	@Column(name = "month", nullable = false, length = 7)
	var month: YearMonth,

	// Dinero disponible en cuentas de ahorro.
	@Column(name = "savings_account", nullable = false, precision = 19, scale = 2)
	var savingsAccount: BigDecimal = BigDecimal.ZERO,

	// Dinero separado en hucha u otros contenedores similares.
	@Column(name = "piggy_bank", nullable = false, precision = 19, scale = 2)
	var piggyBank: BigDecimal = BigDecimal.ZERO,

	// Dinero disponible en cuenta corriente.
	@Column(name = "checking_account", nullable = false, precision = 19, scale = 2)
	var checkingAccount: BigDecimal = BigDecimal.ZERO,

	// Efectivo fisico disponible.
	@Column(name = "cash", nullable = false, precision = 19, scale = 2)
	var cash: BigDecimal = BigDecimal.ZERO,
) {
	fun totalAssets(): BigDecimal = savingsAccount + piggyBank + checkingAccount + cash
}
