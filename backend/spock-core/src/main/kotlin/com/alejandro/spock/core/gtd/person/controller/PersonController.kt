package com.alejandro.spock.core.gtd.person.controller

import com.alejandro.spock.core.gtd.person.dto.CreatePersonRequest
import com.alejandro.spock.core.gtd.person.dto.PersonResponse
import com.alejandro.spock.core.gtd.person.dto.UpdatePersonRequest
import com.alejandro.spock.core.gtd.person.service.PersonService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class PersonController(
	private val personService: PersonService,
) {
	@GetMapping("/people")
	fun listPeople(): List<PersonResponse> =
		personService.listPeople()

	@PostMapping("/people")
	@ResponseStatus(HttpStatus.CREATED)
	fun createPerson(@Valid @RequestBody request: CreatePersonRequest): PersonResponse =
		personService.createPerson(request)

	@PatchMapping("/people/{id}")
	fun updatePerson(@PathVariable id: UUID, @Valid @RequestBody request: UpdatePersonRequest): PersonResponse =
		personService.updatePerson(id, request)
}
