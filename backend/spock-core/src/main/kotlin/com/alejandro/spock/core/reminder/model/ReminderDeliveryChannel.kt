package com.alejandro.spock.core.reminder.model

enum class ReminderDeliveryChannel {
	// Recordatorio interno pendiente de ser mostrado por una interfaz de Spock.
	SPOCK_INTERNAL,

	// Mensaje enviado al usuario por Telegram.
	TELEGRAM,

	// Notificacion local de escritorio mediante una app o helper de macOS.
	DESKTOP_NOTIFICATION,

	// Recordatorio sincronizado con Apple Reminders.
	APPLE_REMINDERS,

	// Evento o aviso sincronizado con Apple Calendar.
	APPLE_CALENDAR,

	// Correo electronico enviado como recordatorio.
	EMAIL,
}
