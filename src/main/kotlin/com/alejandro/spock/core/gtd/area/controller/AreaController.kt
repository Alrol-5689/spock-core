package com.alejandro.spock.core.gtd.area.controller

import com.alejandro.spock.core.gtd.area.dto.AreaResponse
import com.alejandro.spock.core.gtd.area.dto.CreateAreaRequest
import com.alejandro.spock.core.gtd.area.dto.UpdateAreaRequest
import com.alejandro.spock.core.gtd.area.service.AreaService
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
class AreaController(
	private val areaService: AreaService,
) {
	@GetMapping("/areas")
	fun listAreas(): List<AreaResponse> =
		areaService.listAreas()

	@PostMapping("/areas")
	@ResponseStatus(HttpStatus.CREATED)
	fun createArea(@Valid @RequestBody request: CreateAreaRequest): AreaResponse =
		areaService.createArea(request)

	@PatchMapping("/areas/{id}")
	fun updateArea(@PathVariable id: UUID, @Valid @RequestBody request: UpdateAreaRequest): AreaResponse =
		areaService.updateArea(id, request)
}
