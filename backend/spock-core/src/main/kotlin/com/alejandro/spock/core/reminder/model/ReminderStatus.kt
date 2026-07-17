package com.alejandro.spock.core.reminder.model

enum class ReminderStatus {
	// Pendiente de entrega.
	PENDING,

	// Entregado correctamente.
	SENT,

	// Cancelado por el usuario o por una regla de negocio.
	CANCELLED,

	// Fallo al entregar o sincronizar.
	FAILED,
}
