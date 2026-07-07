package com.alejandro.spock.core.gtd.tag.repository

import com.alejandro.spock.core.gtd.tag.model.EntityTag
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EntityTagRepository : JpaRepository<EntityTag, UUID> {
	fun findAllByEntityId(entityId: UUID): List<EntityTag>
}
