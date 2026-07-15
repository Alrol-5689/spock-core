package com.alejandro.spock.core.gtd.agenda.dto

import com.alejandro.spock.core.gtd.daily.dto.DailyLogResponse
import com.alejandro.spock.core.gtd.event.dto.EventResponse
import com.alejandro.spock.core.gtd.habit.dto.HabitOccurrenceResponse
import com.alejandro.spock.core.gtd.project.dto.ProjectResponse
import com.alejandro.spock.core.gtd.task.dto.TaskResponse
import com.alejandro.spock.core.reminder.dto.ReminderResponse
import java.time.LocalDate

data class TodayAgendaResponse(
	val date: LocalDate,
	val tasks: List<TaskResponse>,
	val projects: List<ProjectResponse>,
	val events: List<EventResponse>,
	val reminders: List<ReminderResponse>,
	val habitOccurrences: List<HabitOccurrenceResponse>,
	val dailyLog: DailyLogResponse?,
)
