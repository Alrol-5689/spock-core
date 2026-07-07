package com.alejandro.spock.core.gtd.relation.repository

import com.alejandro.spock.core.gtd.relation.model.EntityRelation
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EntityRelationRepository : JpaRepository<EntityRelation, UUID> {
	fun findAllBySourceEntityIdOrTargetEntityId(sourceEntityId: UUID, targetEntityId: UUID): List<EntityRelation>
}
