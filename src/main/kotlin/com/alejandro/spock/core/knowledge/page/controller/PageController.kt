package com.alejandro.spock.core.knowledge.page.controller

import com.alejandro.spock.core.knowledge.page.dto.AttachFileToPageRequest
import com.alejandro.spock.core.knowledge.page.dto.CreatePageRequest
import com.alejandro.spock.core.knowledge.page.dto.PageFileResponse
import com.alejandro.spock.core.knowledge.page.dto.PageResponse
import com.alejandro.spock.core.knowledge.page.dto.UpdatePageRequest
import com.alejandro.spock.core.knowledge.page.service.PageService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class PageController(
	private val pageService: PageService,
) {
	@GetMapping("/pages")
	fun listPages(): List<PageResponse> =
		pageService.listPages()

	@PostMapping("/pages")
	@ResponseStatus(HttpStatus.CREATED)
	fun createPage(@Valid @RequestBody request: CreatePageRequest): PageResponse =
		pageService.createPage(request)

	@GetMapping("/pages/{id}")
	fun getPage(@PathVariable id: UUID): PageResponse =
		pageService.getPage(id)

	@GetMapping("/entities/{entityId}/page")
	fun getPageByEntity(@PathVariable entityId: UUID): PageResponse =
		pageService.getPageByEntity(entityId)

	@PatchMapping("/pages/{id}")
	fun updatePage(@PathVariable id: UUID, @Valid @RequestBody request: UpdatePageRequest): PageResponse =
		pageService.updatePage(id, request)

	@GetMapping("/page-files")
	fun listPageFiles(@RequestParam pageId: UUID): List<PageFileResponse> =
		pageService.listPageFiles(pageId)

	@PostMapping("/page-files")
	@ResponseStatus(HttpStatus.CREATED)
	fun attachFileToPage(@Valid @RequestBody request: AttachFileToPageRequest): PageFileResponse =
		pageService.attachFileToPage(request)
}
