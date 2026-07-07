package com.alejandro.spock.core.gtd.daily.model

import com.alejandro.spock.core.shared.model.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "daily_logs")
class DailyLog(
	// Mismo identificador que la entidad base asociada.
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Entidad conceptual que permite asociar una pagina Markdown al dia.
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId
	@JoinColumn(name = "id", nullable = false)
	var entity: BaseEntity,

	// Dia representado por este registro.
	@Column(name = "log_date", nullable = false, unique = true)
	var logDate: LocalDate,
)
