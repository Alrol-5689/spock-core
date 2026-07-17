package com.alejandro.spock.core.knowledge.file.model

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
@Table(name = "files")
class ManagedFile(
	// Identificador unico del archivo conocido por Spock.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Ruta actual del archivo en el sistema de archivos.
	@Column(name = "file_path", nullable = false, unique = true)
	var filePath: String,

	// Nombre visible dentro de Spock, independiente del nombre fisico.
	@Column(name = "display_name")
	var displayName: String? = null,

	// Nombre que tenía el archivo cuando fue registrado o importado.
	@Column(name = "original_filename")
	var originalFilename: String? = null,

	// Tipo general de archivo para filtros y tratamiento inicial.
	@Enumerated(EnumType.STRING)
	@Column(name = "file_kind", nullable = false)
	var fileKind: FileKind,

	// Tipo MIME detectado si está disponible.
	@Column(name = "mime_type")
	var mimeType: String? = null,

	// Tamano del archivo en bytes.
	@Column(name = "size_bytes")
	var sizeBytes: Long? = null,

	// Hash SHA-256 usado para reconocer el archivo aunque cambie de ruta.
	@Column(name = "checksum_sha256")
	var checksumSha256: String? = null,

	// Indica si Spock gestiona el archivo, lo importa o solo lo referencia.
	@Enumerated(EnumType.STRING)
	@Column(name = "storage_mode", nullable = false)
	var storageMode: FileStorageMode = FileStorageMode.REFERENCED,

	// Ultima fecha en la que Spock encontro el archivo en su ruta.
	@Column(name = "last_seen_at")
	var lastSeenAt: OffsetDateTime? = null,

	// Fecha desde la que Spock considera que el archivo está desaparecido.
	@Column(name = "missing_at")
	var missingAt: OffsetDateTime? = null,

	// Fecha en la que Spock registro el archivo por primera vez.
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: OffsetDateTime? = null,

	// Fecha de la ultima modificacion de metadata del archivo.
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
