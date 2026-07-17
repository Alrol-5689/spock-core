package com.alejandro.spock.core.gtd.tag.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "tags")
class Tag(
	// Identificador unico de la etiqueta.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Nombre normalizado de la etiqueta.
	@Column(name = "name", nullable = false, unique = true)
	var name: String,

	// Fecha en la que Spock creo la etiqueta.
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: OffsetDateTime? = null,
) {
	@PrePersist
	fun prePersist() {
		createdAt = OffsetDateTime.now()
	}
}
