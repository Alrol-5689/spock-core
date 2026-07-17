package com.alejandro.spock.core.gtd.task.model

import com.alejandro.spock.core.shared.model.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "tasks")
class Task(
	// Mismo identificador que la entidad base asociada.
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Entidad conceptual que permite relacionar la tarea con el resto de Spock.
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId
	@JoinColumn(name = "id", nullable = false)
	var entity: BaseEntity,

	// Estado operativo de la tarea.
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	var status: TaskStatus = TaskStatus.OPEN,

	// Prioridad opcional para ordenar o filtrar acciones.
	@Enumerated(EnumType.STRING)
	@Column(name = "priority")
	var priority: TaskPriority? = null,

	// Fecha limite de la tarea si existe.
	@Column(name = "due_at")
	var dueAt: OffsetDateTime? = null,

	// Fecha en la que se planea trabajar la tarea.
	@Column(name = "scheduled_at")
	var scheduledAt: OffsetDateTime? = null,

	// Fecha en la que la tarea fue completada.
	@Column(name = "completed_at")
	var completedAt: OffsetDateTime? = null,
)
