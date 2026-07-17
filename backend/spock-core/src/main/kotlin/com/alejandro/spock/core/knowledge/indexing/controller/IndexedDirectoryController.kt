package com.alejandro.spock.core.knowledge.indexing.controller

import com.alejandro.spock.core.knowledge.indexing.dto.CreateIndexedDirectoryRequest
import com.alejandro.spock.core.knowledge.indexing.dto.IndexedDirectoryResponse
import com.alejandro.spock.core.knowledge.indexing.dto.UpdateIndexedDirectoryRequest
import com.alejandro.spock.core.knowledge.indexing.service.IndexedDirectoryService
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
class IndexedDirectoryController(
	private val indexedDirectoryService: IndexedDirectoryService,
) {
	@GetMapping("/indexed-directories")
	fun listIndexedDirectories(): List<IndexedDirectoryResponse> =
		indexedDirectoryService.listIndexedDirectories()

	@PostMapping("/indexed-directories")
	@ResponseStatus(HttpStatus.CREATED)
	fun createIndexedDirectory(@Valid @RequestBody request: CreateIndexedDirectoryRequest): IndexedDirectoryResponse =
		indexedDirectoryService.createIndexedDirectory(request)

	@PatchMapping("/indexed-directories/{id}")
	fun updateIndexedDirectory(
		@PathVariable id: UUID,
		@Valid @RequestBody request: UpdateIndexedDirectoryRequest,
	): IndexedDirectoryResponse =
		indexedDirectoryService.updateIndexedDirectory(id, request)
}
