package com.alejandro.spock.core.knowledge.page.model

import com.alejandro.spock.core.knowledge.file.model.ManagedFile
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
@Table(name = "page_files")
class PageFile(
	// Identificador unico de la asociacion entre página y archivo.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Pagina que referencia o adjunta el archivo.
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "page_id", nullable = false)
	var page: Page,

	// Archivo asociado a la página.
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "file_id", nullable = false)
	var file: ManagedFile,

	// Fecha en la que Spock creo la asociación.
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: OffsetDateTime? = null,
) {
	@PrePersist
	fun prePersist() {
		createdAt = OffsetDateTime.now()
	}
}
