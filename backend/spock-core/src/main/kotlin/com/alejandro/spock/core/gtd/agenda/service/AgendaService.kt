package com.alejandro.spock.core.gtd.agenda.service

import com.alejandro.spock.core.gtd.agenda.dto.TodayAgendaResponse
import com.alejandro.spock.core.gtd.daily.service.DailyLogService
import com.alejandro.spock.core.gtd.event.service.EventService
import com.alejandro.spock.core.gtd.habit.service.HabitService
import com.alejandro.spock.core.gtd.project.service.ProjectService
import com.alejandro.spock.core.gtd.task.service.TaskService
import com.alejandro.spock.core.reminder.service.ReminderService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.ZoneOffset

@Service
class AgendaService(
	private val taskService: TaskService,
	private val projectService: ProjectService,
	private val eventService: EventService,
	private val reminderService: ReminderService,
	private val habitService: HabitService,
	private val dailyLogService: DailyLogService,
) {
	@Transactional(readOnly = true)
	fun today(date: LocalDate = LocalDate.now(ZoneOffset.UTC)): TodayAgendaResponse =
		TodayAgendaResponse(
			date = date,
			tasks = taskService.listTodayTasks(date),
			projects = projectService.listOpenProjects(),
			events = eventService.listEventsForDate(date),
			reminders = reminderService.listDueReminders(date.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC)),
			habitOccurrences = habitService.listOccurrencesForDate(date),
			dailyLog = dailyLogService.findDailyLogByDate(date),
		)
}
