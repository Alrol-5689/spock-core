package com.alejandro.spock.core.finance.transaction.model

import com.alejandro.spock.core.finance.account.model.FinancialAccount
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "finance_transactions")
class FinancialTransaction(
	// Identificador unico de la transaccion financiera.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Cuenta financiera asociada si se conoce.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id")
	var account: FinancialAccount? = null,

	// Nombre visible de la transaccion.
	@Column(name = "name", nullable = false)
	var name: String,

	// Descripcion libre de la transaccion.
	@Column(name = "description")
	var description: String? = null,

	// Importe monetario de la transaccion.
	@Column(name = "amount", nullable = false, precision = 19, scale = 2)
	var amount: BigDecimal,

	// Indica si la transaccion es ingreso o gasto.
	@Enumerated(EnumType.STRING)
	@Column(name = "direction", nullable = false)
	var direction: TransactionDirection,

	// Categoria especifica cuando la transaccion es un ingreso.
	@Enumerated(EnumType.STRING)
	@Column(name = "income_category")
	var incomeCategory: IncomeCategory? = null,

	// Categoria especifica cuando la transaccion es un gasto.
	@Enumerated(EnumType.STRING)
	@Column(name = "expense_category")
	var expenseCategory: ExpenseCategory? = null,

	// Fecha economica en la que ocurrio la transaccion.
	@Column(name = "transaction_date", nullable = false)
	var transactionDate: LocalDate = LocalDate.now(),

	// Fecha en la que Spock registro la transaccion.
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: OffsetDateTime? = null,

	// Fecha de la última modificacion de la transaccion.
	@Column(name = "updated_at", nullable = false)
	var updatedAt: OffsetDateTime? = null,
) {
	@PrePersist
	fun prePersist() {
		val now = OffsetDateTime.now()
		createdAt = now
		updatedAt = now
	}

	@PreUpdate
	fun preUpdate() {
		updatedAt = OffsetDateTime.now()
	}
}
