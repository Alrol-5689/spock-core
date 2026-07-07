package com.alejandro.spock.core.knowledge.file.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "file_indexes")
class FileIndex(
	// Identificador del archivo indexado.
	@Id
	@Column(name = "file_id", nullable = false, updatable = false)
	var fileId: UUID? = null,

	// Archivo al que pertenece este estado tecnico de indexacion.
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId
	@JoinColumn(name = "file_id", nullable = false)
	var file: ManagedFile,

	// Fecha en la que el archivo fue indexado por última vez.
	@Column(name = "indexed_at")
	var indexedAt: OffsetDateTime? = null,

	// Fecha de la ultima sincronizacion entre archivo e indice.
	@Column(name = "last_synced_at")
	var lastSyncedAt: OffsetDateTime? = null,

	// Hash del contenido usado para saber si el índice está actualizado.
	@Column(name = "content_hash")
	var contentHash: String? = null,

	// Estado del proceso de OCR para archivos que lo necesiten.
	@Enumerated(EnumType.STRING)
	@Column(name = "ocr_status")
	var ocrStatus: IndexStatus? = null,

	// Ruta del texto OCR extraido si se guarda como archivo externo.
	@Column(name = "ocr_text_path")
	var ocrTextPath: String? = null,

	// Estado del proceso de embeddings.
	@Enumerated(EnumType.STRING)
	@Column(name = "embedding_status")
	var embeddingStatus: IndexStatus? = null,

	// Identificador externo o interno del embedding generado.
	@Column(name = "embedding_id")
	var embeddingId: String? = null,

	// Version del esquema de indexacion usado para este archivo.
	@Column(name = "index_version")
	var indexVersion: Int? = null,

	// Ultimo error producido durante indexacion o sincronizacion.
	@Column(name = "error_message")
	var errorMessage: String? = null,
)
