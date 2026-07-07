package com.alejandro.spock.core.gtd.habit.repository

import com.alejandro.spock.core.gtd.habit.model.HabitOccurrence
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface HabitOccurrenceRepository : JpaRepository<HabitOccurrence, UUID> {
	fun findAllByHabitIdAndDueDateBetweenOrderByDueDateAsc(
		habitId: UUID,
		from: LocalDate,
		to: LocalDate,
	): List<HabitOccurrence>
}
