package com.alejandro.spock.core.gtd.agenda.controller

import com.alejandro.spock.core.gtd.agenda.dto.TodayAgendaResponse
import com.alejandro.spock.core.gtd.agenda.service.AgendaService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.ZoneOffset

@RestController
class AgendaController(
	private val agendaService: AgendaService,
) {
	@GetMapping("/agenda/today")
	fun today(@RequestParam(required = false) date: LocalDate?): TodayAgendaResponse =
		agendaService.today(date ?: LocalDate.now(ZoneOffset.UTC))
}
