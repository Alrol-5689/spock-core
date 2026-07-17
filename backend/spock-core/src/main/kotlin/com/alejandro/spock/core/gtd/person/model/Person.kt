package com.alejandro.spock.core.gtd.person.model

import com.alejandro.spock.core.shared.model.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "people")
class Person(
	// Mismo identificador que la entidad base asociada.
    @Id
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Entidad conceptual que permite relacionar la persona con eventos, proyectos o notas.
    @OneToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId
	@JoinColumn(name = "id", nullable = false)
	var entity: BaseEntity,

	// Nombre mostrado para la persona dentro de Spock.
    @Column(name = "display_name", nullable = false)
	var displayName: String,

	// Correo electronico principal si se conoce.
    @Column(name = "email")
	var email: String? = null,

	// Telefono principal si se conoce.
    @Column(name = "phone")
	var phone: String? = null,
)