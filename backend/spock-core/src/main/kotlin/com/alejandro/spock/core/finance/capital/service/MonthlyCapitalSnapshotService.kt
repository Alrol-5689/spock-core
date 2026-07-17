package com.alejandro.spock.core.finance.capital.service

import com.alejandro.spock.core.finance.capital.dto.CreateMonthlyCapitalSnapshotRequest
import com.alejandro.spock.core.finance.capital.dto.MonthlyCapitalSnapshotResponse
import com.alejandro.spock.core.finance.capital.model.MonthlyCapitalSnapshot
import com.alejandro.spock.core.finance.capital.repository.MonthlyCapitalSnapshotRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class MonthlyCapitalSnapshotService(
	private val snapshotRepository: MonthlyCapitalSnapshotRepository,
) {
	@Transactional(readOnly = true)
	fun listSnapshots(): List<MonthlyCapitalSnapshotResponse> =
		snapshotRepository.findAllByOrderByMonthDesc().map { it.toResponse() }

	@Transactional
	fun createSnapshot(request: CreateMonthlyCapitalSnapshotRequest): MonthlyCapitalSnapshotResponse {
		snapshotRepository.findByMonth(request.month)?.let {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Snapshot for month ${request.month} already exists")
		}
		return snapshotRepository.save(
			MonthlyCapitalSnapshot(
				month = request.month,
				savingsAccount = request.savingsAccount,
				piggyBank = request.piggyBank,
				checkingAccount = request.checkingAccount,
				cash = request.cash,
			),
		).toResponse()
	}

	private fun MonthlyCapitalSnapshot.toResponse(): MonthlyCapitalSnapshotResponse =
		MonthlyCapitalSnapshotResponse(id = requiredId(), version = version, month = month, savingsAccount = savingsAccount, piggyBank = piggyBank, checkingAccount = checkingAccount, cash = cash, totalAssets = totalAssets())

	private fun MonthlyCapitalSnapshot.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted snapshot has no id")
}
