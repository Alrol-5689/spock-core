package com.alejandro.spock.core.gtd.person.service

import com.alejandro.spock.core.gtd.person.dto.CreatePersonRequest
import com.alejandro.spock.core.gtd.person.dto.PersonResponse
import com.alejandro.spock.core.gtd.person.dto.UpdatePersonRequest
import com.alejandro.spock.core.gtd.person.model.Person
import com.alejandro.spock.core.gtd.person.repository.PersonRepository
import com.alejandro.spock.core.shared.model.entity.BaseEntity
import com.alejandro.spock.core.shared.model.entity.EntityType
import com.alejandro.spock.core.shared.repository.BaseEntityRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class PersonService(
	private val personRepository: PersonRepository,
	private val baseEntityRepository: BaseEntityRepository,
) {
	@Transactional(readOnly = true)
	fun listPeople(): List<PersonResponse> =
		personRepository.findAll().map { it.toResponse() }.sortedBy { it.displayName }

	@Transactional
	fun createPerson(request: CreatePersonRequest): PersonResponse {
		val entity = baseEntityRepository.save(BaseEntity(entityType = EntityType.PERSON, title = request.displayName, summary = request.summary))
		return personRepository.save(Person(entity = entity, displayName = request.displayName, email = request.email, phone = request.phone)).toResponse()
	}

	@Transactional
	fun updatePerson(id: UUID, request: UpdatePersonRequest): PersonResponse {
		val person = person(id)
		request.displayName?.let {
			person.displayName = it
			person.entity.title = it
		}
		request.summary?.let { person.entity.summary = it }
		request.email?.let { person.email = it }
		request.phone?.let { person.phone = it }
		return personRepository.save(person).toResponse()
	}

	private fun person(id: UUID): Person =
		personRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Person $id not found") }

	private fun Person.toResponse(): PersonResponse =
		PersonResponse(id = requiredId(), displayName = displayName, summary = entity.summary, email = email, phone = phone, createdAt = entity.createdAt, updatedAt = entity.updatedAt)

	private fun Person.requiredId(): UUID =
		id ?: throw IllegalStateException("Persisted person has no id")
}
