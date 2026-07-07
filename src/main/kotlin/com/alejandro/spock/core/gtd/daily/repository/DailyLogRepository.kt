package com.alejandro.spock.core.gtd.daily.repository

import com.alejandro.spock.core.gtd.daily.model.DailyLog
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface DailyLogRepository : JpaRepository<DailyLog, UUID> {
	fun findByLogDate(logDate: LocalDate): DailyLog?

	fun findAllByLogDateBetweenOrderByLogDateAsc(from: LocalDate, to: LocalDate): List<DailyLog>
}
