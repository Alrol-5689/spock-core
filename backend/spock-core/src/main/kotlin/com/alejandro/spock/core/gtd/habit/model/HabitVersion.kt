package com.alejandro.spock.core.gtd.habit.model

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
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "habit_versions")
class HabitVersion(
	// Identificador de la version concreta de reglas.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Habito al que pertenece esta version.
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "habit_id", nullable = false)
	var habit: Habit,

	// Primer dia en el que estas reglas son aplicables.
	@Column(name = "starts_on", nullable = false)
	var startsOn: LocalDate,

	// Ultimo dia de vigencia. Null significa vigente hasta nuevo cambio.
	@Column(name = "ends_on")
	var endsOn: LocalDate? = null,

	// Tipo de frecuencia usado para generar ocurrencias.
	@Enumerated(EnumType.STRING)
	@Column(name = "frequency_type", nullable = false)
	var frequencyType: HabitFrequencyType,

	// Objetivo numerico cuando la regla lo necesita, por ejemplo 3 veces por semana.
	@Column(name = "target_count")
	var targetCount: Int? = null,

	// Dias aplicables en texto simple inicial, por ejemplo MON,WED.
	@Column(name = "weekdays")
	var weekdays: String? = null,

	// Permite desactivar esta version sin borrar su historia.
	@Column(name = "active", nullable = false)
	var active: Boolean = true,

	// Fecha de creacion de la version.
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: OffsetDateTime? = null,

	// Fecha de ultima modificacion de la version.
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
