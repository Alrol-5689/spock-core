package com.alejandro.spock.core.gtd.habit.repository

import com.alejandro.spock.core.gtd.habit.model.HabitVersion
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface HabitVersionRepository : JpaRepository<HabitVersion, UUID> {
	fun findAllByHabitIdOrderByStartsOnDesc(habitId: UUID): List<HabitVersion>
}
