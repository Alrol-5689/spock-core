package com.alejandro.spock.core.gtd.tag.service

import com.alejandro.spock.core.gtd.tag.dto.ApplyTagRequest
import com.alejandro.spock.core.gtd.tag.dto.CreateTagRequest
import com.alejandro.spock.core.gtd.tag.dto.EntityTagResponse
import com.alejandro.spock.core.gtd.tag.dto.TagResponse
import com.alejandro.spock.core.gtd.tag.model.EntityTag
import com.alejandro.spock.core.gtd.tag.model.Tag
import com.alejandro.spock.core.gtd.tag.repository.EntityTagRepository
import com.alejandro.spock.core.gtd.tag.repository.TagRepository
import com.alejandro.spock.core.shared.model.entity.BaseEntity
import com.alejandro.spock.core.shared.repository.BaseEntityRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class TagService(
	private val tagRepository: TagRepository,
	private val entityTagRepository: EntityTagRepository,
	private val baseEntityRepository: BaseEntityRepository,
) {
	@Transactional(readOnly = true)
	fun listTags(): List<TagResponse> =
		tagRepository.findAllByOrderByNameAsc().map { it.toResponse() }

	@Transactional
	fun createTag(request: CreateTagRequest): TagResponse {
		val normalizedName = request.name.trim()
		tagRepository.findByName(normalizedName)?.let { return it.toResponse() }
		return tagRepository.save(Tag(name = normalizedName)).toResponse()
	}

	@Transactional
	fun applyTag(request: ApplyTagRequest): EntityTagResponse {
		val entity = ensureEntity(request.entityId)
		val tag = tagRepository.findById(request.tagId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Tag ${request.tagId} not found") }
		return entityTagRepository.save(EntityTag(entity = entity, tag = tag)).toResponse()
	}

	@Transactional(readOnly = true)
	fun listEntityTags(entityId: UUID): List<EntityTagResponse> {
		ensureEntity(entityId)
		return entityTagRepository.findAllByEntityId(entityId).map { it.toResponse() }
	}

	private fun ensureEntity(id: UUID): BaseEntity =
		baseEntityRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Entity $id not found") }

	private fun Tag.toResponse(): TagResponse =
		TagResponse(id = requiredId(), name = name, createdAt = createdAt)

	private fun EntityTag.toResponse(): EntityTagResponse =
		EntityTagResponse(id = requiredId(), entityId = entity.requiredId(), tagId = tag.requiredId(), tagName = tag.name, createdAt = createdAt)

	private fun Tag.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted tag has no id")

	private fun EntityTag.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted entity tag has no id")

	private fun BaseEntity.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted entity has no id")
}
