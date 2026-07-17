package com.alejandro.spock.core.gtd.event.model

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
@Table(name = "events")
class Event(
	// Mismo identificador que la entidad base asociada.
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Entidad conceptual que permite relacionar el evento con personas, tareas o notas.
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId
	@JoinColumn(name = "id", nullable = false)
	var entity: BaseEntity,

	// Momento de inicio del evento.
	@Column(name = "starts_at", nullable = false)
	var startsAt: OffsetDateTime,

	// Momento de fin del evento si se conoce.
	@Column(name = "ends_at")
	var endsAt: OffsetDateTime? = null,

	// Clasificacion funcional del evento para filtros, vistas y automatizaciones.
	@Enumerated(EnumType.STRING)
	@Column(name = "event_type", nullable = false)
	var eventType: EventType = EventType.OTHER,

	// Indica que el evento ocupa el dia completo y no una hora concreta.
	@Column(name = "all_day", nullable = false)
	var allDay: Boolean = false,

	// Lugar fisico o virtual asociado al evento.
	@Column(name = "location")
	var location: String? = null,
)
