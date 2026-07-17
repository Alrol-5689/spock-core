package com.alejandro.spock.core.reminder.model

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
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "reminders")
class Reminder(
	// Identificador unico del recordatorio.
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	var id: UUID? = null,

	// Entidad de Spock a la que pertenece el recordatorio, si existe.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_id")
	var entity: BaseEntity? = null,

	// Momento exacto en el que Spock debe activar el recordatorio.
	@Column(name = "remind_at", nullable = false)
	var remindAt: OffsetDateTime,

	// Título corto opcional; si no existe, Spock puede usar el título de la entidad asociada.
	@Column(name = "title")
	var title: String? = null,

	// Mensaje opcional con más contexto para el usuario.
	@Column(name = "message")
	var message: String? = null,

	// Estado actual del recordatorio dentro de Spock.
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	var status: ReminderStatus = ReminderStatus.PENDING,

	// Canal preferido por el que Spock avisara al usuario.
	@Enumerated(EnumType.STRING)
	@Column(name = "delivery_channel", nullable = false)
	var deliveryChannel: ReminderDeliveryChannel = ReminderDeliveryChannel.SPOCK_INTERNAL,

	// Proveedor externo sincronizado si existe, como Apple Reminders o Telegram.
	@Enumerated(EnumType.STRING)
	@Column(name = "external_provider")
	var externalProvider: ReminderExternalProvider? = null,

	// Identificador del recordatorio en el proveedor externo si existe.
	@Column(name = "external_id")
	var externalId: String? = null,

	// Fecha en la que Spock creo el recordatorio.
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: OffsetDateTime? = null,

	// Fecha de la ultima modificacion del recordatorio.
	@Column(name = "updated_at", nullable = false)
	var updatedAt: OffsetDateTime? = null,

	// Fecha en la que Spock considero enviado el recordatorio.
	@Column(name = "sent_at")
	var sentAt: OffsetDateTime? = null,

	// Fecha en la que el recordatorio fue cancelado.
	@Column(name = "cancelled_at")
	var cancelledAt: OffsetDateTime? = null,

	// Ultimo error producido al intentar entregar o sincronizar el recordatorio.
	@Column(name = "last_error")
	var lastError: String? = null,
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
