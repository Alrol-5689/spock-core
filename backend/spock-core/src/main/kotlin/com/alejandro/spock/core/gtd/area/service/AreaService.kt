package com.alejandro.spock.core.gtd.area.service

import com.alejandro.spock.core.gtd.area.dto.AreaResponse
import com.alejandro.spock.core.gtd.area.dto.CreateAreaRequest
import com.alejandro.spock.core.gtd.area.dto.UpdateAreaRequest
import com.alejandro.spock.core.gtd.area.model.Area
import com.alejandro.spock.core.gtd.area.repository.AreaRepository
import com.alejandro.spock.core.shared.model.entity.BaseEntity
import com.alejandro.spock.core.shared.model.entity.EntityType
import com.alejandro.spock.core.shared.repository.BaseEntityRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class AreaService(
	private val areaRepository: AreaRepository,
	private val baseEntityRepository: BaseEntityRepository,
) {
	@Transactional(readOnly = true)
	fun listAreas(): List<AreaResponse> =
		areaRepository.findAll().map { it.toResponse() }.sortedBy { it.title }

	@Transactional
	fun createArea(request: CreateAreaRequest): AreaResponse {
		val entity = baseEntityRepository.save(
			BaseEntity(entityType = EntityType.AREA, title = request.title, summary = request.summary, status = request.status.name),
		)
		return areaRepository.save(Area(entity = entity, type = request.type, status = request.status)).toResponse()
	}

	@Transactional
	fun updateArea(id: UUID, request: UpdateAreaRequest): AreaResponse {
		val area = area(id)
		request.title?.let { area.entity.title = it }
		request.summary?.let { area.entity.summary = it }
		request.type?.let { area.type = it }
		request.status?.let {
			area.status = it
			area.entity.status = it.name
		}
		return areaRepository.save(area).toResponse()
	}

	private fun area(id: UUID): Area =
		areaRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Area $id not found") }

	private fun Area.toResponse(): AreaResponse =
		AreaResponse(id = requiredId(), title = entity.title, summary = entity.summary, type = type, status = status, createdAt = entity.createdAt, updatedAt = entity.updatedAt)

	private fun Area.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted area has no id")
}
