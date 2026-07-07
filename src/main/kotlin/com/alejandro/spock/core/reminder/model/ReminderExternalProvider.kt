package com.alejandro.spock.core.reminder.model

enum class ReminderExternalProvider {
	// Proveedor propio de Spock sin servicio externo.
	SPOCK,

	// Aplicacion Apple Reminders.
	APPLE_REMINDERS,

	// Aplicacion Apple Calendar.
	APPLE_CALENDAR,

	// Bot o integracion de Telegram.
	TELEGRAM,

	// Proveedor de correo electronico.
	EMAIL,
}
