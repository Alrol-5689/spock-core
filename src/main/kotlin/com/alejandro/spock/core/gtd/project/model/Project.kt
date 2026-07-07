package com.alejandro.spock.core.gtd.project.model

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
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "projects")
class Project(
	// Mismo identificador que la entidad base asociada.
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Entidad conceptual que permite relacionar el proyecto con tareas, notas y áreas.
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId
	@JoinColumn(name = "id", nullable = false)
	var entity: BaseEntity,

	// Estado del proyecto dentro del flujo GTD.
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	var status: ProjectStatus = ProjectStatus.ACTIVE,

	// Fecha de inicio real o estimada del proyecto.
	@Column(name = "started_at")
	var startedAt: LocalDate? = null,

	// Fecha objetivo o limite del proyecto.
	@Column(name = "due_date")
	var dueDate: LocalDate? = null,

	// Fecha en la que el proyecto termino.
	@Column(name = "ended_at")
	var endedAt: LocalDate? = null,
)
