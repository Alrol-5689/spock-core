package com.alejandro.spock.core.gtd.relation.controller

import com.alejandro.spock.core.gtd.relation.dto.CreateRelationRequest
import com.alejandro.spock.core.gtd.relation.dto.RelationResponse
import com.alejandro.spock.core.gtd.relation.service.RelationService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class RelationController(
	private val relationService: RelationService,
) {
	@GetMapping("/relations")
	fun listRelations(@RequestParam entityId: UUID): List<RelationResponse> =
		relationService.listRelations(entityId)

	@PostMapping("/relations")
	@ResponseStatus(HttpStatus.CREATED)
	fun createRelation(@Valid @RequestBody request: CreateRelationRequest): RelationResponse =
		relationService.createRelation(request)
}
