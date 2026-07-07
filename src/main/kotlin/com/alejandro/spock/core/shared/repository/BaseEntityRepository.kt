package com.alejandro.spock.core.shared.repository

import com.alejandro.spock.core.shared.model.entity.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BaseEntityRepository : JpaRepository<BaseEntity, UUID>
