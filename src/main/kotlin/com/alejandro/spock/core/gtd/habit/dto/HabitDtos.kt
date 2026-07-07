package com.alejandro.spock.core.gtd.habit.dto

import com.alejandro.spock.core.gtd.habit.model.HabitFrequencyType
import com.alejandro.spock.core.gtd.habit.model.HabitOccurrenceStatus
import com.alejandro.spock.core.gtd.habit.model.HabitValueType
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class CreateHabitRequest(
	@field:NotBlank
	val name: String,
	val description: String? = null,
	val projectId: UUID? = null,
	val valueType: HabitValueType = HabitValueType.BOOLEAN,
	val unit: String? = null,
	val active: Boolean = true,
	@field:Valid
	val initialVersion: CreateHabitVersionRequest? = null,
)

data class UpdateHabitRequest(
	val name: String? = null,
	val description: String? = null,
	val projectId: UUID? = null,
	val clearProject: Boolean = false,
	val valueType: HabitValueType? = null,
	val unit: String? = null,
	val clearUnit: Boolean = false,
	val active: Boolean? = null,
)

data class CreateHabitVersionRequest(
	@field:NotNull
	val startsOn: LocalDate,
	val endsOn: LocalDate? = null,
	@field:NotNull
	val frequencyType: HabitFrequencyType,
	@field:Positive
	val targetCount: Int? = null,
	val weekdays: String? = null,
	val active: Boolean = true,
)

data class CreateHabitOccurrenceRequest(
	@field:NotNull
	val habitVersionId: UUID,
	@field:NotNull
	val dueDate: LocalDate,
	val status: HabitOccurrenceStatus = HabitOccurrenceStatus.PENDING,
	val disabled: Boolean = false,
	val skippedReason: String? = null,
	val notes: String? = null,
	val numericValue: BigDecimal? = null,
	val countValue: Int? = null,
	val durationSeconds: Int? = null,
	val textValue: String? = null,
	val recordedAt: OffsetDateTime? = null,
)

data class UpdateHabitOccurrenceRequest(
	@field:NotNull
	val status: HabitOccurrenceStatus,
	val disabled: Boolean? = null,
	val skippedReason: String? = null,
	val notes: String? = null,
	val numericValue: BigDecimal? = null,
	val countValue: Int? = null,
	val durationSeconds: Int? = null,
	val textValue: String? = null,
	val recordedAt: OffsetDateTime? = null,
)

data class HabitResponse(
	val id: UUID,
	val projectId: UUID?,
	val name: String,
	val description: String?,
	val valueType: HabitValueType,
	val unit: String?,
	val active: Boolean,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
	val archivedAt: OffsetDateTime?,
)

data class HabitVersionResponse(
	val id: UUID,
	val habitId: UUID,
	val startsOn: LocalDate,
	val endsOn: LocalDate?,
	val frequencyType: HabitFrequencyType,
	val targetCount: Int?,
	val weekdays: String?,
	val active: Boolean,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
)

data class HabitOccurrenceResponse(
	val id: UUID,
	val habitId: UUID,
	val habitVersionId: UUID,
	val dueDate: LocalDate,
	val status: HabitOccurrenceStatus,
	val disabled: Boolean,
	val skippedReason: String?,
	val notes: String?,
	val numericValue: BigDecimal?,
	val countValue: Int?,
	val durationSeconds: Int?,
	val textValue: String?,
	val recordedAt: OffsetDateTime?,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
)
