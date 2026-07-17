package com.alejandro.spock.core.reminder.dto

import com.alejandro.spock.core.reminder.model.ReminderDeliveryChannel
import com.alejandro.spock.core.reminder.model.ReminderExternalProvider
import com.alejandro.spock.core.reminder.model.ReminderStatus
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.util.UUID

data class CreateReminderRequest(
	val entityId: UUID? = null,
	@field:NotNull val remindAt: OffsetDateTime,
	val title: String? = null,
	val message: String? = null,
	val status: ReminderStatus = ReminderStatus.PENDING,
	val deliveryChannel: ReminderDeliveryChannel = ReminderDeliveryChannel.SPOCK_INTERNAL,
	val externalProvider: ReminderExternalProvider? = null,
	val externalId: String? = null,
)

data class UpdateReminderRequest(
	val remindAt: OffsetDateTime? = null,
	val title: String? = null,
	val message: String? = null,
	val status: ReminderStatus? = null,
	val deliveryChannel: ReminderDeliveryChannel? = null,
	val externalProvider: ReminderExternalProvider? = null,
	val externalId: String? = null,
	val sentAt: OffsetDateTime? = null,
	val cancelledAt: OffsetDateTime? = null,
	val lastError: String? = null,
)

data class ReminderResponse(
	val id: UUID,
	val entityId: UUID?,
	val remindAt: OffsetDateTime,
	val title: String?,
	val message: String?,
	val status: ReminderStatus,
	val deliveryChannel: ReminderDeliveryChannel,
	val externalProvider: ReminderExternalProvider?,
	val externalId: String?,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
	val sentAt: OffsetDateTime?,
	val cancelledAt: OffsetDateTime?,
	val lastError: String?,
)
