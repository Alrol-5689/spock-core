package com.alejandro.spock.core.finance.reimbursement.repository

import com.alejandro.spock.core.finance.reimbursement.model.Reimbursement
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ReimbursementRepository : JpaRepository<Reimbursement, UUID>
