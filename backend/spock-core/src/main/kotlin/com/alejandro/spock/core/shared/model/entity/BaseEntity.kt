package com.alejandro.spock.core.shared.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "entities")
class BaseEntity(
	// Identificador global de cualquier objeto relacionable de Spock.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Tipo funcional de la entidad: tarea, proyecto, evento, persona, etc.
	@Enumerated(EnumType.STRING)
	@Column(name = "entity_type", nullable = false)
	var entityType: EntityType,

	// Titulo principal visible para el usuario.
	@Column(name = "title", nullable = false)
	var title: String,

	// Identificador legible opcional para URLs, rutas o busquedas humanas.
	@Column(name = "slug")
	var slug: String? = null,

	// Resumen corto de la entidad sin obligar a crear una página Markdown.
	@Column(name = "summary")
	var summary: String? = null,

	// Estado generico cuando el dominio específico no necesita un campo propio.
	@Column(name = "status")
	var status: String? = null,

	// Fecha en la que Spock registro la entidad por primera vez.
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: OffsetDateTime? = null,

	// Fecha de la ultima modificacion registrada por Spock.
	@Column(name = "updated_at", nullable = false)
	var updatedAt: OffsetDateTime? = null,

	// Fecha de archivado logico sin borrar la entidad ni sus relaciones.
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
