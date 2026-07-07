package com.alejandro.spock.core.reminder.repository

import com.alejandro.spock.core.reminder.model.Reminder
import com.alejandro.spock.core.reminder.model.ReminderStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ReminderRepository : JpaRepository<Reminder, UUID> {
	fun findAllByStatusOrderByRemindAtAsc(status: ReminderStatus): List<Reminder>
	fun findAllByOrderByRemindAtAsc(): List<Reminder>
}
