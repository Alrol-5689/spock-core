package com.alejandro.spock.core.gtd.habit.repository

import com.alejandro.spock.core.gtd.habit.model.Habit
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface HabitRepository : JpaRepository<Habit, UUID> {
	fun findAllByArchivedAtIsNullOrderByNameAsc(): List<Habit>
}
