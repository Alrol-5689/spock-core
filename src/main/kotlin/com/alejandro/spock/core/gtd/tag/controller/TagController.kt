package com.alejandro.spock.core.gtd.tag.controller

import com.alejandro.spock.core.gtd.tag.dto.ApplyTagRequest
import com.alejandro.spock.core.gtd.tag.dto.CreateTagRequest
import com.alejandro.spock.core.gtd.tag.dto.EntityTagResponse
import com.alejandro.spock.core.gtd.tag.dto.TagResponse
import com.alejandro.spock.core.gtd.tag.service.TagService
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
class TagController(
	private val tagService: TagService,
) {
	@GetMapping("/tags")
	fun listTags(): List<TagResponse> =
		tagService.listTags()

	@PostMapping("/tags")
	@ResponseStatus(HttpStatus.CREATED)
	fun createTag(@Valid @RequestBody request: CreateTagRequest): TagResponse =
		tagService.createTag(request)

	@GetMapping("/entity-tags")
	fun listEntityTags(@RequestParam entityId: UUID): List<EntityTagResponse> =
		tagService.listEntityTags(entityId)

	@PostMapping("/entity-tags")
	@ResponseStatus(HttpStatus.CREATED)
	fun applyTag(@Valid @RequestBody request: ApplyTagRequest): EntityTagResponse =
		tagService.applyTag(request)
}
