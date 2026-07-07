package com.alejandro.spock.core.gtd.relation.service

import com.alejandro.spock.core.gtd.relation.dto.CreateRelationRequest
import com.alejandro.spock.core.gtd.relation.dto.RelationResponse
import com.alejandro.spock.core.gtd.relation.model.EntityRelation
import com.alejandro.spock.core.gtd.relation.repository.EntityRelationRepository
import com.alejandro.spock.core.shared.model.entity.BaseEntity
import com.alejandro.spock.core.shared.repository.BaseEntityRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class RelationService(
	private val relationRepository: EntityRelationRepository,
	private val baseEntityRepository: BaseEntityRepository,
) {
	@Transactional(readOnly = true)
	fun listRelations(entityId: UUID): List<RelationResponse> {
		ensureEntity(entityId)
		return relationRepository.findAllBySourceEntityIdOrTargetEntityId(entityId, entityId).map { it.toResponse() }
	}

	@Transactional
	fun createRelation(request: CreateRelationRequest): RelationResponse {
		val source = ensureEntity(request.sourceEntityId)
		val target = ensureEntity(request.targetEntityId)
		return relationRepository.save(
			EntityRelation(sourceEntity = source, targetEntity = target, relationType = request.relationType),
		).toResponse()
	}

	private fun ensureEntity(id: UUID): BaseEntity =
		baseEntityRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Entity $id not found") }

	private fun EntityRelation.toResponse(): RelationResponse =
		RelationResponse(id = requiredId(), sourceEntityId = sourceEntity.requiredId(), targetEntityId = targetEntity.requiredId(), relationType = relationType, createdAt = createdAt)

	private fun EntityRelation.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted relation has no id")

	private fun BaseEntity.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted entity has no id")
}
