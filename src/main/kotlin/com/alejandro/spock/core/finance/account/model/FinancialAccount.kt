package com.alejandro.spock.core.finance.account.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "finance_accounts")
class FinancialAccount(
	// Identificador unico de la cuenta financiera.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Nombre visible de la cuenta.
	@Column(name = "name", nullable = false)
	var name: String,

	// Tipo de cuenta o contenedor de dinero.
	@Enumerated(EnumType.STRING)
	@Column(name = "account_type", nullable = false)
	var accountType: FinancialAccountType,

	// Entidad financiera o plataforma asociada si existe.
	@Column(name = "institution")
	var institution: String? = null,

	// Moneda principal de la cuenta en formato ISO de tres letras.
	@Column(name = "currency", nullable = false, length = 3)
	var currency: String = "EUR",

	// Saldo inicial usado como punto de partida para calculos.
	@Column(name = "initial_balance", nullable = false, precision = 19, scale = 2)
	var initialBalance: BigDecimal = BigDecimal.ZERO,

	// Indica si la cuenta sigue activa para nuevas operaciones.
	@Column(name = "is_active", nullable = false)
	var isActive: Boolean = true,

	// Fecha en la que Spock creo la cuenta.
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: OffsetDateTime? = null,

	// Fecha de la ultima modificacion de la cuenta.
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
