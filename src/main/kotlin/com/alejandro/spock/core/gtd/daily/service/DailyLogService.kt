package com.alejandro.spock.core.gtd.daily.service

import com.alejandro.spock.core.gtd.daily.dto.CreateDailyLogRequest
import com.alejandro.spock.core.gtd.daily.dto.DailyLogResponse
import com.alejandro.spock.core.gtd.daily.dto.UpdateDailyLogRequest
import com.alejandro.spock.core.gtd.daily.model.DailyLog
import com.alejandro.spock.core.gtd.daily.repository.DailyLogRepository
import com.alejandro.spock.core.knowledge.page.model.Page
import com.alejandro.spock.core.knowledge.page.repository.PageRepository
import com.alejandro.spock.core.shared.model.entity.BaseEntity
import com.alejandro.spock.core.shared.model.entity.EntityType
import com.alejandro.spock.core.shared.repository.BaseEntityRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.util.UUID

@Service
class DailyLogService(
	private val dailyLogRepository: DailyLogRepository,
	private val baseEntityRepository: BaseEntityRepository,
	private val pageRepository: PageRepository,
) {
	@Transactional(readOnly = true)
	fun listDailyLogs(from: LocalDate?, to: LocalDate?): List<DailyLogResponse> {
		if ((from == null) != (to == null)) {
			throw badRequest("from and to must be provided together")
		}
		if (from != null && to != null) {
			if (to.isBefore(from)) {
				throw badRequest("to cannot be before from")
			}
			return dailyLogRepository.findAllByLogDateBetweenOrderByLogDateAsc(from, to).map { it.toResponse() }
		}
		return dailyLogRepository.findAll().map { it.toResponse() }.sortedBy { it.logDate }
	}

	@Transactional
	fun createDailyLog(request: CreateDailyLogRequest): DailyLogResponse {
		dailyLogRepository.findByLogDate(request.logDate)?.let {
			throw ResponseStatusException(HttpStatus.CONFLICT, "Daily log ${request.logDate} already exists")
		}
		val title = request.title ?: request.logDate.toString()
		val entity = baseEntityRepository.save(
			BaseEntity(
				entityType = EntityType.DAILY_LOG,
				title = title,
				summary = request.summary,
				status = "ACTIVE",
			),
		)
		val dailyLog = dailyLogRepository.save(DailyLog(entity = entity, logDate = request.logDate))
		request.markdownPath?.let { createOrUpdatePage(entity, title, it) }
		return dailyLog.toResponse()
	}

	@Transactional(readOnly = true)
	fun getDailyLog(id: UUID): DailyLogResponse =
		dailyLog(id).toResponse()

	@Transactional(readOnly = true)
	fun getDailyLogByDate(logDate: LocalDate): DailyLogResponse =
		(dailyLogRepository.findByLogDate(logDate) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Daily log $logDate not found")).toResponse()

	@Transactional
	fun updateDailyLog(id: UUID, request: UpdateDailyLogRequest): DailyLogResponse {
		val dailyLog = dailyLog(id)
		request.title?.let { dailyLog.entity.title = it }
		request.summary?.let { dailyLog.entity.summary = it }
		request.markdownPath?.let { createOrUpdatePage(dailyLog.entity, dailyLog.entity.title, it) }
		return dailyLogRepository.save(dailyLog).toResponse()
	}

	private fun createOrUpdatePage(entity: BaseEntity, title: String, markdownPath: String): Page {
		val entityId = entity.requiredId()
		pageRepository.findByMarkdownPath(markdownPath)?.let { existing ->
			if (existing.entity.requiredId() != entityId) {
				throw ResponseStatusException(HttpStatus.CONFLICT, "Page path $markdownPath already belongs to another entity")
			}
		}
		val page = pageRepository.findByEntityId(entityId)
		return if (page == null) {
			pageRepository.save(Page(entity = entity, title = title, markdownPath = markdownPath))
		} else {
			page.title = title
			page.markdownPath = markdownPath
			pageRepository.save(page)
		}
	}

	private fun dailyLog(id: UUID): DailyLog =
		dailyLogRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Daily log $id not found") }

	private fun DailyLog.toResponse(): DailyLogResponse {
		val page = pageRepository.findByEntityId(requiredId())
		return DailyLogResponse(
			id = requiredId(),
			logDate = logDate,
			title = entity.title,
			summary = entity.summary,
			pageId = page?.requiredId(),
			markdownPath = page?.markdownPath,
			createdAt = entity.createdAt,
			updatedAt = entity.updatedAt,
		)
	}

	private fun DailyLog.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted daily log has no id")

	private fun Page.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted page has no id")

	private fun BaseEntity.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted entity has no id")

	private fun badRequest(message: String): ResponseStatusException =
		ResponseStatusException(HttpStatus.BAD_REQUEST, message)
}
