package com.alejandro.spock.core.knowledge.file.controller

import com.alejandro.spock.core.knowledge.file.dto.CreateManagedFileRequest
import com.alejandro.spock.core.knowledge.file.dto.ManagedFileResponse
import com.alejandro.spock.core.knowledge.file.service.ManagedFileService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ManagedFileController(
	private val managedFileService: ManagedFileService,
) {
	@GetMapping("/files")
	fun listFiles(): List<ManagedFileResponse> =
		managedFileService.listFiles()

	@PostMapping("/files")
	@ResponseStatus(HttpStatus.CREATED)
	fun createFile(@Valid @RequestBody request: CreateManagedFileRequest): ManagedFileResponse =
		managedFileService.createFile(request)
}
