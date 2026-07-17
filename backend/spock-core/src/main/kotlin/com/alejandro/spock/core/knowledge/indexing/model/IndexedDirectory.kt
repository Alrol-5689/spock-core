package com.alejandro.spock.core.knowledge.indexing.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "indexed_directories")
class IndexedDirectory(
	// Identificador unico de la carpeta conocida por Spock.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Ruta de la carpeta que Spock puede escanear.
	@Column(name = "path", nullable = false, unique = true)
	var path: String,

	// Indica si la carpeta esta activa para indexacion.
	@Column(name = "enabled", nullable = false)
	var enabled: Boolean = true,

	// Indica si Spock debe escanear subcarpetas.
	@Column(name = "recursive", nullable = false)
	var recursive: Boolean = true,

	// Fecha del ultimo escaneo completado sobre esta carpeta.
	@Column(name = "last_scanned_at")
	var lastScannedAt: OffsetDateTime? = null,

	// Fecha en la que Spock registro esta carpeta.
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: OffsetDateTime? = null,

	// Fecha de la ultima modificacion de la configuracion de esta carpeta.
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
