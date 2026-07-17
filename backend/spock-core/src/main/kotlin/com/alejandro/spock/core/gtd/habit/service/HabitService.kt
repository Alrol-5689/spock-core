package com.alejandro.spock.core.gtd.habit.service

import com.alejandro.spock.core.gtd.habit.dto.CreateHabitOccurrenceRequest
import com.alejandro.spock.core.gtd.habit.dto.CreateHabitRequest
import com.alejandro.spock.core.gtd.habit.dto.CreateHabitVersionRequest
import com.alejandro.spock.core.gtd.habit.dto.HabitOccurrenceResponse
import com.alejandro.spock.core.gtd.habit.dto.HabitResponse
import com.alejandro.spock.core.gtd.habit.dto.HabitVersionResponse
import com.alejandro.spock.core.gtd.habit.dto.UpdateHabitOccurrenceRequest
import com.alejandro.spock.core.gtd.habit.dto.UpdateHabitRequest
import com.alejandro.spock.core.gtd.habit.model.Habit
import com.alejandro.spock.core.gtd.habit.model.HabitOccurrence
import com.alejandro.spock.core.gtd.habit.model.HabitOccurrenceStatus
import com.alejandro.spock.core.gtd.habit.model.HabitValueType
import com.alejandro.spock.core.gtd.habit.model.HabitVersion
import com.alejandro.spock.core.gtd.habit.repository.HabitOccurrenceRepository
import com.alejandro.spock.core.gtd.habit.repository.HabitRepository
import com.alejandro.spock.core.gtd.habit.repository.HabitVersionRepository
import com.alejandro.spock.core.gtd.project.repository.ProjectRepository
import com.alejandro.spock.core.shared.model.entity.BaseEntity
import com.alejandro.spock.core.shared.model.entity.EntityType
import com.alejandro.spock.core.shared.repository.BaseEntityRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Service
class HabitService(
	private val habitRepository: HabitRepository,
	private val habitVersionRepository: HabitVersionRepository,
	private val habitOccurrenceRepository: HabitOccurrenceRepository,
	private val projectRepository: ProjectRepository,
	private val baseEntityRepository: BaseEntityRepository,
) {
	@Transactional(readOnly = true)
	fun listHabits(): List<HabitResponse> =
		habitRepository.findAllByArchivedAtIsNullOrderByNameAsc().map { it.toResponse() }

	@Transactional
	fun createHabit(request: CreateHabitRequest): HabitResponse {
		val project = request.projectId?.let { projectId ->
			projectRepository.findById(projectId).orElseThrow {
				notFound("Project $projectId not found")
			}
		}
		val entity = baseEntityRepository.save(
			BaseEntity(
				entityType = EntityType.HABIT,
				title = request.name,
				summary = request.description,
				status = if (request.active) "ACTIVE" else "INACTIVE",
			),
		)
		val habit = habitRepository.save(
			Habit(
				entity = entity,
				project = project,
				name = request.name,
				description = request.description,
				valueType = request.valueType,
				unit = request.unit,
				active = request.active,
			),
		)

		request.initialVersion?.let { createVersion(habit.requiredId(), it) }

		return habit.toResponse()
	}

	@Transactional(readOnly = true)
	fun getHabit(habitId: UUID): HabitResponse =
		getHabitEntity(habitId).toResponse()

	@Transactional
	fun updateHabit(habitId: UUID, request: UpdateHabitRequest): HabitResponse {
		val habit = getHabitEntity(habitId)
		request.name?.let {
			habit.name = it
			habit.entity.title = it
		}
		request.description?.let {
			habit.description = it
			habit.entity.summary = it
		}
		if (request.clearProject) {
			habit.project = null
		} else {
			request.projectId?.let { projectId ->
				habit.project = projectRepository.findById(projectId).orElseThrow {
					notFound("Project $projectId not found")
				}
			}
		}
		request.valueType?.let { habit.valueType = it }
		if (request.clearUnit) {
			habit.unit = null
		} else {
			request.unit?.let { habit.unit = it }
		}
		request.active?.let {
			habit.active = it
			habit.entity.status = if (it) "ACTIVE" else "INACTIVE"
		}
		return habitRepository.save(habit).toResponse()
	}

	@Transactional(readOnly = true)
	fun listVersions(habitId: UUID): List<HabitVersionResponse> {
		getHabitEntity(habitId)
		return habitVersionRepository.findAllByHabitIdOrderByStartsOnDesc(habitId).map { it.toResponse() }
	}

	@Transactional
	fun createVersion(habitId: UUID, request: CreateHabitVersionRequest): HabitVersionResponse {
		if (request.endsOn != null && request.endsOn.isBefore(request.startsOn)) {
			throw badRequest("endsOn cannot be before startsOn")
		}
		val habit = getHabitEntity(habitId)
		val version = habitVersionRepository.save(
			HabitVersion(
				habit = habit,
				startsOn = request.startsOn,
				endsOn = request.endsOn,
				frequencyType = request.frequencyType,
				targetCount = request.targetCount,
				weekdays = request.weekdays,
				active = request.active,
			),
		)
		return version.toResponse()
	}

	@Transactional(readOnly = true)
	fun listOccurrences(habitId: UUID, from: LocalDate, to: LocalDate): List<HabitOccurrenceResponse> {
		if (to.isBefore(from)) {
			throw badRequest("to cannot be before from")
		}
		getHabitEntity(habitId)
		return habitOccurrenceRepository
			.findAllByHabitIdAndDueDateBetweenOrderByDueDateAsc(habitId, from, to)
			.map { it.toResponse() }
	}

	@Transactional(readOnly = true)
	fun listOccurrencesForDate(date: LocalDate): List<HabitOccurrenceResponse> =
		habitOccurrenceRepository.findAllByDueDateBetweenOrderByDueDateAsc(date, date).map { it.toResponse() }

	@Transactional
	fun createOccurrence(habitId: UUID, request: CreateHabitOccurrenceRequest): HabitOccurrenceResponse {
		val habit = getHabitEntity(habitId)
		val version = habitVersionRepository.findById(request.habitVersionId).orElseThrow {
			notFound("Habit version ${request.habitVersionId} not found")
		}
		if (version.habit.requiredId() != habit.requiredId()) {
			throw badRequest("Habit version does not belong to habit $habitId")
		}
		validateOccurrenceValue(habit, request.numericValue, request.countValue, request.durationSeconds, request.textValue)
		val occurrence = habitOccurrenceRepository.save(
			HabitOccurrence(
				habit = habit,
				habitVersion = version,
				dueDate = request.dueDate,
				status = request.status,
				disabled = request.disabled || request.status == HabitOccurrenceStatus.DISABLED,
				skippedReason = request.skippedReason,
				notes = request.notes,
				numericValue = request.numericValue,
				countValue = request.countValue,
				durationSeconds = request.durationSeconds,
				textValue = request.textValue,
				recordedAt = request.recordedAt,
			),
		)
		return occurrence.toResponse()
	}

	@Transactional
	fun updateOccurrence(occurrenceId: UUID, request: UpdateHabitOccurrenceRequest): HabitOccurrenceResponse {
		val occurrence = habitOccurrenceRepository.findById(occurrenceId).orElseThrow {
			notFound("Habit occurrence $occurrenceId not found")
		}
		validateOccurrenceValue(occurrence.habit, request.numericValue, request.countValue, request.durationSeconds, request.textValue)
		occurrence.status = request.status
		occurrence.disabled = request.disabled ?: (request.status == HabitOccurrenceStatus.DISABLED)
		occurrence.skippedReason = request.skippedReason
		occurrence.notes = request.notes
		occurrence.numericValue = request.numericValue
		occurrence.countValue = request.countValue
		occurrence.durationSeconds = request.durationSeconds
		occurrence.textValue = request.textValue
		occurrence.recordedAt = request.recordedAt
		return habitOccurrenceRepository.save(occurrence).toResponse()
	}

	private fun validateOccurrenceValue(
		habit: Habit,
		numericValue: BigDecimal?,
		countValue: Int?,
		durationSeconds: Int?,
		textValue: String?,
	) {
		val provided = listOfNotNull(numericValue, countValue, durationSeconds, textValue).size
		if (provided > 1) {
			throw badRequest("Only one occurrence value can be provided")
		}
		when (habit.valueType) {
			HabitValueType.BOOLEAN -> if (provided > 0) {
				throw badRequest("Boolean habits cannot include typed values")
			}
			HabitValueType.NUMBER -> if (countValue != null || durationSeconds != null || textValue != null) {
				throw badRequest("Number habits must use numericValue")
			}
			HabitValueType.COUNT -> if (numericValue != null || durationSeconds != null || textValue != null) {
				throw badRequest("Count habits must use countValue")
			}
			HabitValueType.DURATION -> if (numericValue != null || countValue != null || textValue != null) {
				throw badRequest("Duration habits must use durationSeconds")
			}
			HabitValueType.TEXT -> if (numericValue != null || countValue != null || durationSeconds != null) {
				throw badRequest("Text habits must use textValue")
			}
		}
		if (countValue != null && countValue < 0) {
			throw badRequest("countValue cannot be negative")
		}
		if (durationSeconds != null && durationSeconds < 0) {
			throw badRequest("durationSeconds cannot be negative")
		}
	}

	private fun getHabitEntity(habitId: UUID): Habit =
		habitRepository.findById(habitId).orElseThrow {
			notFound("Habit $habitId not found")
		}

	private fun Habit.toResponse(): HabitResponse =
		HabitResponse(
			id = requiredId(),
			projectId = project?.id,
			name = name,
			description = description,
			valueType = valueType,
			unit = unit,
			active = active,
			createdAt = createdAt,
			updatedAt = updatedAt,
			archivedAt = archivedAt,
		)

	private fun HabitVersion.toResponse(): HabitVersionResponse =
		HabitVersionResponse(
			id = requiredId(),
			habitId = habit.requiredId(),
			startsOn = startsOn,
			endsOn = endsOn,
			frequencyType = frequencyType,
			targetCount = targetCount,
			weekdays = weekdays,
			active = active,
			createdAt = createdAt,
			updatedAt = updatedAt,
		)

	private fun HabitOccurrence.toResponse(): HabitOccurrenceResponse =
		HabitOccurrenceResponse(
			id = requiredId(),
			habitId = habit.requiredId(),
			habitVersionId = habitVersion.requiredId(),
			dueDate = dueDate,
			status = status,
			disabled = disabled,
			skippedReason = skippedReason,
			notes = notes,
			numericValue = numericValue,
			countValue = countValue,
			durationSeconds = durationSeconds,
			textValue = textValue,
			recordedAt = recordedAt,
			createdAt = createdAt,
			updatedAt = updatedAt,
		)

	private fun Habit.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted habit has no id")

	private fun HabitVersion.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted habit version has no id")

	private fun HabitOccurrence.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted habit occurrence has no id")

	private fun notFound(message: String): ResponseStatusException =
		ResponseStatusException(HttpStatus.NOT_FOUND, message)

	private fun badRequest(message: String): ResponseStatusException =
		ResponseStatusException(HttpStatus.BAD_REQUEST, message)
}
