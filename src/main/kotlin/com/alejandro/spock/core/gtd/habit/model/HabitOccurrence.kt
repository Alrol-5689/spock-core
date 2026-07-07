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
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "habit_occurrences")
class HabitOccurrence(
	// Identificador del registro concreto.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Habito evaluado en esta fecha.
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "habit_id", nullable = false)
	var habit: Habit,

	// Version de reglas que genero esta expectativa.
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "habit_version_id", nullable = false)
	var habitVersion: HabitVersion,

	// Fecha concreta que toca evaluar.
	@Column(name = "due_date", nullable = false)
	var dueDate: LocalDate,

	// Resultado del dia.
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	var status: HabitOccurrenceStatus = HabitOccurrenceStatus.PENDING,

	// Marca explicita para registros no evaluables.
	@Column(name = "disabled", nullable = false)
	var disabled: Boolean = false,

	// Motivo humano de una omision justificada.
	@Column(name = "skipped_reason")
	var skippedReason: String? = null,

	// Notas libres del dia.
	@Column(name = "notes")
	var notes: String? = null,

	// Valor decimal para mediciones como peso, horas o distancia.
	@Column(name = "numeric_value", precision = 12, scale = 4)
	var numericValue: BigDecimal? = null,

	// Valor entero para conteos como pasos, paginas o repeticiones.
	@Column(name = "count_value")
	var countValue: Int? = null,

	// Duracion registrada en segundos.
	@Column(name = "duration_seconds")
	var durationSeconds: Int? = null,

	// Valor textual corto para registros cualitativos.
	@Column(name = "text_value")
	var textValue: String? = null,

	// Momento en el que se capturó el valor real, si difiere de la fecha evaluada.
	@Column(name = "recorded_at")
	var recordedAt: OffsetDateTime? = null,

	// Fecha de creacion del registro.
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: OffsetDateTime? = null,

	// Fecha de ultima modificacion del registro.
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
