package com.alejandro.spock.core.finance.account.repository

import com.alejandro.spock.core.finance.account.model.FinancialAccount
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FinancialAccountRepository : JpaRepository<FinancialAccount, UUID> {
	fun findAllByOrderByNameAsc(): List<FinancialAccount>
}
