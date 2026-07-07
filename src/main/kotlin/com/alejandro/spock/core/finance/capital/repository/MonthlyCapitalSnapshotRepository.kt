package com.alejandro.spock.core.finance.capital.repository

import com.alejandro.spock.core.finance.capital.model.MonthlyCapitalSnapshot
import org.springframework.data.jpa.repository.JpaRepository
import java.time.YearMonth
import java.util.UUID

interface MonthlyCapitalSnapshotRepository : JpaRepository<MonthlyCapitalSnapshot, UUID> {
	fun findAllByOrderByMonthDesc(): List<MonthlyCapitalSnapshot>
	fun findByMonth(month: YearMonth): MonthlyCapitalSnapshot?
}
