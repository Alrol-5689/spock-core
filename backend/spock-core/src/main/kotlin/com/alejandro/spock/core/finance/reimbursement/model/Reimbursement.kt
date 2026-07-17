package com.alejandro.spock.core.finance.reimbursement.model

import com.alejandro.spock.core.finance.transaction.model.FinancialTransaction
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "finance_reimbursements")
class Reimbursement(
	// Identificador unico del reembolso.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Transaccion de gasto a la que compensa este reembolso.
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "transaction_id", nullable = false)
	var transaction: FinancialTransaction,

	// Importe recuperado.
	@Column(name = "amount", nullable = false, precision = 19, scale = 2)
	var amount: BigDecimal,

	// Persona o entidad que hizo el reembolso.
	@Column(name = "payer_name")
	var payerName: String? = null,

	// Nota libre sobre el reembolso.
	@Column(name = "note")
	var note: String? = null,

	// Fecha en la que se recibio o registro el reembolso.
	@Column(name = "reimbursement_date", nullable = false)
	var reimbursementDate: LocalDate = LocalDate.now(),
)
