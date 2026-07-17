package com.alejandro.spock.core.gtd.relation.model

enum class RelationType {
	// La entidad origen pertenece al proyecto indicado como entidad destino.
	BELONGS_TO_PROJECT,

	// La entidad origen es subtarea de la tarea indicada como entidad destino.
	SUBTASK_OF,

	// La entidad origen pertenece al área indicada como entidad destino.
	BELONGS_TO_AREA,

	// La entidad origen está relacionada con el evento indicado como entidad destino.
	RELATED_TO_EVENT,

	// La entidad origen está relacionada con la nota indicada como entidad destino.
	RELATED_TO_NOTE,

	// La entidad origen implica o involucra a la persona indicada como entidad destino.
	INVOLVES_PERSON,

	// La entidad origen depende de la entidad destino para poder completarse.
	DEPENDS_ON,

	// La entidad origen bloquea el avance de la entidad destino.
	BLOCKS,

	// Relacion generica cuando no existe un tipo más especifico.
	RELATED_TO,
}
