package com.alejandro.spock.core.gtd.area.model

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
import java.util.UUID

@Entity
@Table(name = "areas")
class Area(
	// Mismo identificador que la entidad base asociada.
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Entidad conceptual que permite relacionar el area con proyectos, tareas y notas.
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId
	@JoinColumn(name = "id", nullable = false)
	var entity: BaseEntity,

	// Tipo general del area para agrupar responsabilidades en dashboards.
	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	var type: AreaType = AreaType.PERSONAL,

	// Estado del área de responsabilidad.
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	var status: AreaStatus = AreaStatus.ACTIVE,
)
