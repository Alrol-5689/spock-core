# Modelo de datos

## Estado

Este documento es exploratorio.

No define todavia un esquema definitivo de PostgreSQL.

## Base de datos

La idea inicial es utilizar una unica base de datos PostgreSQL para todo Spock.

PostgreSQL sera la fuente de verdad para los datos estructurados del sistema.

## Entidades candidatas

Posibles tablas o conceptos:

- entidades
- proyectos
- tareas
- habitos
- eventos
- documentos
- recordatorios
- personas
- etiquetas
- relaciones

Estos nombres no deben asumirse como definitivos.

## Modelo conceptual inicial

Una opcion razonable es partir de una tabla generica de entidades y tablas especificas cuando exista comportamiento propio.

Ejemplo conceptual:

```text
entities
  id
  type
  title
  status
  created_at
  updated_at

documents
  id
  entity_id
  path
  title
  format
  created_at
  updated_at

relations
  id
  source_entity_id
  target_entity_id
  relation_type

tags
  id
  name

entity_tags
  entity_id
  tag_id
```

## Ventajas del enfoque

- permite relacionar distintos tipos de informacion
- evita duplicar campos comunes
- facilita una busqueda unificada
- permite crecer hacia tipos especificos

## Inconvenientes del enfoque

- una tabla demasiado generica puede ocultar reglas de dominio
- puede volverse dificil validar datos especificos
- existe riesgo de crear un modelo abstracto antes de entender los casos reales

## Alternativa

Crear tablas concretas desde el principio:

- projects
- tasks
- habits
- events
- people
- reminders
- documents

Ventajas:

- modelo mas explicito
- validaciones mas claras
- consultas mas directas

Inconvenientes:

- mas duplicacion inicial
- relaciones transversales menos uniformes
- puede requerir refactors cuando aparezcan nuevos tipos

## Recomendacion inicial actual

Empezar con un modelo mixto parecido a Notion:

- `entities` como identidad comun de todo objeto importante
- tablas especificas para dominios con comportamiento claro, como `projects`, `tasks`, `habits`, `events` y `people`
- `pages` como pagina Markdown opcional asociada a cualquier entidad
- `files` como archivos fisicos registrados por Spock
- `page_files` para adjuntar archivos a paginas
- `entity_relations` para relacionar entidades entre si
- `tags` y `entity_tags` para clasificacion transversal

La regla principal es:

```text
Todo lo importante puede ser una entity.
Cada entity puede tener una page Markdown opcional.
Los files se adjuntan a pages.
Las relaciones semanticas se hacen entre entities.
```

El resumen detallado del modelo recomendado esta en [11_postgresql_y_metadata.md](11_postgresql_y_metadata.md).

El modelado de habitos debe distinguir entre:

```text
Proyecto = objetivo grande
Tarea = accion puntual
Habito = comportamiento recurrente medible
Ocurrencia = instancia concreta de un habito en una fecha
```

Los habitos pueden asociarse a proyectos, pero sus estadisticas deben vivir en tablas propias con versiones de reglas y ocurrencias fechadas para no romper historicos cuando cambien frecuencia, dias u objetivos.

## Decisiones pendientes

- herramienta de migraciones
- convencion de nombres
- estrategia de IDs
- uso de UUID o enteros
- auditoria de cambios
- soft delete
- busqueda full-text
- versionado de documentos
