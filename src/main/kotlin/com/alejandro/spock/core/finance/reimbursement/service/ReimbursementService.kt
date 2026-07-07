package com.alejandro.spock.core.finance.reimbursement.service

import com.alejandro.spock.core.finance.reimbursement.dto.CreateReimbursementRequest
import com.alejandro.spock.core.finance.reimbursement.dto.ReimbursementResponse
import com.alejandro.spock.core.finance.reimbursement.model.Reimbursement
import com.alejandro.spock.core.finance.reimbursement.repository.ReimbursementRepository
import com.alejandro.spock.core.finance.transaction.model.FinancialTransaction
import com.alejandro.spock.core.finance.transaction.repository.FinancialTransactionRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class ReimbursementService(
	private val reimbursementRepository: ReimbursementRepository,
	private val transactionRepository: FinancialTransactionRepository,
) {
	@Transactional
	fun createReimbursement(request: CreateReimbursementRequest): ReimbursementResponse =
		reimbursementRepository.save(
			Reimbursement(
				transaction = transaction(request.transactionId),
				amount = request.amount,
				payerName = request.payerName,
				note = request.note,
				reimbursementDate = request.reimbursementDate,
			),
		).toResponse()

	private fun transaction(id: UUID): FinancialTransaction =
		transactionRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Financial transaction $id not found") }

	private fun Reimbursement.toResponse(): ReimbursementResponse =
		ReimbursementResponse(id = requiredId(), transactionId = transaction.requiredId(), amount = amount, payerName = payerName, note = note, reimbursementDate = reimbursementDate)

	private fun Reimbursement.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted reimbursement has no id")

	private fun FinancialTransaction.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted transaction has no id")
}
