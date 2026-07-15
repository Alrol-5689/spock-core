package com.alejandro.spock.core.reminder.service

import com.alejandro.spock.core.reminder.dto.CreateReminderRequest
import com.alejandro.spock.core.reminder.dto.ReminderResponse
import com.alejandro.spock.core.reminder.dto.UpdateReminderRequest
import com.alejandro.spock.core.reminder.model.Reminder
import com.alejandro.spock.core.reminder.model.ReminderStatus
import com.alejandro.spock.core.reminder.repository.ReminderRepository
import com.alejandro.spock.core.shared.model.entity.BaseEntity
import com.alejandro.spock.core.shared.repository.BaseEntityRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.OffsetDateTime
import java.util.UUID

@Service
class ReminderService(
	private val reminderRepository: ReminderRepository,
	private val baseEntityRepository: BaseEntityRepository,
) {
	@Transactional(readOnly = true)
	fun listReminders(status: ReminderStatus?): List<ReminderResponse> =
		(status?.let { reminderRepository.findAllByStatusOrderByRemindAtAsc(it) }
			?: reminderRepository.findAllByOrderByRemindAtAsc()).map { it.toResponse() }

	@Transactional(readOnly = true)
	fun listDueReminders(until: OffsetDateTime = OffsetDateTime.now()): List<ReminderResponse> =
		reminderRepository
			.findAllByStatusAndRemindAtLessThanEqualOrderByRemindAtAsc(ReminderStatus.PENDING, until)
			.map { it.toResponse() }

	@Transactional
	fun createReminder(request: CreateReminderRequest): ReminderResponse {
		if (request.entityId == null && request.title.isNullOrBlank()) {
			throw badRequest("title or entityId is required")
		}
		val entity = request.entityId?.let { entityId ->
			baseEntityRepository.findById(entityId).orElseThrow { notFound("Entity $entityId not found") }
		}
		return reminderRepository.save(
			Reminder(
				entity = entity,
				remindAt = request.remindAt,
				title = request.title,
				message = request.message,
				status = request.status,
				deliveryChannel = request.deliveryChannel,
				externalProvider = request.externalProvider,
				externalId = request.externalId,
			),
		).toResponse()
	}

	@Transactional(readOnly = true)
	fun getReminder(id: UUID): ReminderResponse =
		reminder(id).toResponse()

	@Transactional
	fun updateReminder(id: UUID, request: UpdateReminderRequest): ReminderResponse {
		val reminder = reminder(id)
		request.remindAt?.let { reminder.remindAt = it }
		request.title?.let { reminder.title = it }
		request.message?.let { reminder.message = it }
		request.status?.let { reminder.status = it }
		request.deliveryChannel?.let { reminder.deliveryChannel = it }
		request.externalProvider?.let { reminder.externalProvider = it }
		request.externalId?.let { reminder.externalId = it }
		request.sentAt?.let { reminder.sentAt = it }
		request.cancelledAt?.let { reminder.cancelledAt = it }
		request.lastError?.let { reminder.lastError = it }
		return reminderRepository.save(reminder).toResponse()
	}

	private fun reminder(id: UUID): Reminder =
		reminderRepository.findById(id).orElseThrow { notFound("Reminder $id not found") }

	private fun Reminder.toResponse(): ReminderResponse =
		ReminderResponse(
			id = requiredId(),
			entityId = entity?.id,
			remindAt = remindAt,
			title = title,
			message = message,
			status = status,
			deliveryChannel = deliveryChannel,
			externalProvider = externalProvider,
			externalId = externalId,
			createdAt = createdAt,
			updatedAt = updatedAt,
			sentAt = sentAt,
			cancelledAt = cancelledAt,
			lastError = lastError,
		)

	private fun Reminder.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted reminder has no id")

	private fun notFound(message: String): ResponseStatusException =
		ResponseStatusException(HttpStatus.NOT_FOUND, message)

	private fun badRequest(message: String): ResponseStatusException =
		ResponseStatusException(HttpStatus.BAD_REQUEST, message)
}
