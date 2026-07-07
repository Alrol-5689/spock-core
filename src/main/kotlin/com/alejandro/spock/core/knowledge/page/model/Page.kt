package com.alejandro.spock.core.knowledge.page.model

import com.alejandro.spock.core.shared.model.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "pages")
class Page(
	// Identificador único de la página Markdown.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Entidad a la que está asociada esta página principal.
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "entity_id", nullable = false)
	var entity: BaseEntity,

	// Título visible de la página.
	@Column(name = "title", nullable = false)
	var title: String,

	// Ruta del archivo Markdown dentro del vault de conocimiento.
	@Column(name = "markdown_path", nullable = false, unique = true)
	var markdownPath: String,

	// Fecha en la que Spock creo la pagina.
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: OffsetDateTime? = null,

	// Fecha de la última modificacion registrada por Spock.
	@Column(name = "updated_at", nullable = false)
	var updatedAt: OffsetDateTime? = null,

	// Fecha de la última sincronizacion entre Markdown y PostgreSQL.
	@Column(name = "last_synced_at")
	var lastSyncedAt: OffsetDateTime? = null,
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
