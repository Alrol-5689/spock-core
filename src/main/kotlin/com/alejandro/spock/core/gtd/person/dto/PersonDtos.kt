package com.alejandro.spock.core.gtd.person.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.OffsetDateTime
import java.util.UUID

data class CreatePersonRequest(
	@field:NotBlank val displayName: String,
	val summary: String? = null,
	@field:Email val email: String? = null,
	val phone: String? = null,
)

data class UpdatePersonRequest(
	val displayName: String? = null,
	val summary: String? = null,
	@field:Email val email: String? = null,
	val phone: String? = null,
)

data class PersonResponse(
	val id: UUID,
	val displayName: String,
	val summary: String?,
	val email: String?,
	val phone: String?,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?,
)
