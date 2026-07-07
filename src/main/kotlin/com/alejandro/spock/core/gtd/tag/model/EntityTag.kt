package com.alejandro.spock.core.gtd.tag.model

import com.alejandro.spock.core.shared.model.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "entity_tags")
class EntityTag(
	// Identificador unico de la asociacion entre entidad y etiqueta.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Entidad etiquetada.
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "entity_id", nullable = false)
	var entity: BaseEntity,

	// Etiqueta aplicada a la entidad.
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "tag_id", nullable = false)
	var tag: Tag,

	// Fecha en la que Spock aplico la etiqueta.
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: OffsetDateTime? = null,
) {
	@PrePersist
	fun prePersist() {
		createdAt = OffsetDateTime.now()
	}
}
