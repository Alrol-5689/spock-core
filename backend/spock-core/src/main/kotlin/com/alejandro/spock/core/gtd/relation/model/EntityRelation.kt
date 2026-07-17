package com.alejandro.spock.core.gtd.relation.model

import com.alejandro.spock.core.shared.model.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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
@Table(name = "entity_relations")
class EntityRelation(
	// Identificador unico de la relacion.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

    // Entidad desde la que parte la relacion (HIJO).
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "source_entity_id", nullable = false)
	var sourceEntity: BaseEntity,

    // Entidad 'destino' de la relación (PADRE).
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "target_entity_id", nullable = false)
	var targetEntity: BaseEntity,

	// Tipo semantico de relación entre ambas entidades.
	@Enumerated(EnumType.STRING)
	@Column(name = "relation_type", nullable = false)
	var relationType: RelationType,

	// Fecha en la que Spock creo la relacion.
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: OffsetDateTime? = null,
) {
	@PrePersist
	fun prePersist() {
		createdAt = OffsetDateTime.now()
	}
}
