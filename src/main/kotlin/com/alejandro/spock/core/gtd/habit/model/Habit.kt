package com.alejandro.spock.core.gtd.habit.model

import com.alejandro.spock.core.gtd.project.model.Project
import com.alejandro.spock.core.shared.model.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "habits")
class Habit(
	// Mismo identificador que la entidad base asociada.
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Entidad conceptual que permite relacionar el habito con notas, areas y otros objetos.
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId
	@JoinColumn(name = "id", nullable = false)
	var entity: BaseEntity,

	// Proyecto al que contribuye el habito, si existe.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	var project: Project? = null,

	// Nombre visible del hábito dentro del tracker.
	@Column(name = "name", nullable = false)
	var name: String,

	// Descripcion humana del comportamiento medible.
	@Column(name = "description")
	var description: String? = null,

	// Tipo de valor que registra el habito en cada ocurrencia.
	@Enumerated(EnumType.STRING)
	@Column(name = "value_type", nullable = false)
	var valueType: HabitValueType = HabitValueType.BOOLEAN,

	// Unidad humana para mediciones, por ejemplo kg, min, pasos o paginas.
	@Column(name = "unit")
	var unit: String? = null,

	// Permite desactivar el hábito hacia adelante sin borrar su historia.
	@Column(name = "active", nullable = false)
	var active: Boolean = true,

	// Fecha en la que Spock registro el hábito por primera vez.
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: OffsetDateTime? = null,

	// Fecha de la última modificacion registrada por Spock.
	@Column(name = "updated_at", nullable = false)
	var updatedAt: OffsetDateTime? = null,

	// Archivado logico sin borrar versiones ni ocurrencias historicas.
	@Column(name = "archived_at")
	var archivedAt: OffsetDateTime? = null,
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
