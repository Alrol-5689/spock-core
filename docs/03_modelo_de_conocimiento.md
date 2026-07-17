# Modelo de conocimiento

## Principio

El conocimiento vive en archivos Markdown.

La base de datos no debe almacenar documentos largos. Debe guardar metadatos, relaciones y referencias que permitan encontrar y conectar documentos.

## Separacion de responsabilidades

Markdown guarda:

- notas
- documentacion personal
- apuntes
- proyectos
- ideas
- documentacion tecnica
- libros
- reuniones
- decisiones personales

PostgreSQL guarda:

- identificadores
- titulos normalizados
- tipos de entidad
- relaciones
- etiquetas
- fechas
- estado
- rutas o referencias a documentos
- indices de busqueda si se decide implementarlos

## Relacion entre entidad y documento

Ejemplo:

```text
Proyecto
  -> documento principal
  -> knowledge/020_Projects/proyecto-x/README.md
```

El usuario no deberia tener que conocer ni escribir rutas manualmente en el uso normal del sistema. La aplicacion debe resolver automaticamente la relacion entre una entidad y su documento Markdown.

## Estructura recomendada del vault

La estructura fisica de `knowledge` no debe copiar la estructura interna de la API. La API puede tener dominios como `gtd`, `knowledge`, `finance` o `reminders`, pero el vault debe organizarse para que una persona pueda leerlo y mantenerlo sin Spock.

Estructura inicial recomendada:

```text
knowledge/
|-- 000_Inbox/
|-- 010_GTD/
|   |-- Tasks.md
|   |-- Events.md
|   |-- Projects.md
|   |-- Waiting For.md
|   |-- Someday Maybe.md
|   `-- Reviews/
|-- 020_Projects/
|-- 030_Areas/
|-- 040_Resources/
|-- 090_Archive/
`-- 100_Templates/
```

Regla principal:

```text
010_GTD = que requiere atencion
020_Projects / 030_Areas / 040_Resources = donde vive el contexto y el conocimiento
```

`010_GTD` debe funcionar como panel operativo e indice de relaciones, no como almacen principal de conocimiento. Por ejemplo, `010_GTD/Tasks.md` puede listar tareas y enlazar a sus proyectos, fuentes o notas asociadas; `010_GTD/Events.md` puede listar eventos y enlazar a la nota completa de la reunion o del evento.

Ejemplo:

```markdown
# Tasks

## Next
- [ ] Revisar paper de clasificacion radar
  Proyecto: [[020_Projects/C-UAS Indra/README]]
  Fuente: [[020_Projects/C-UAS Indra/Papers/paper-radar-2024]]
```

El material real asociado a un proyecto debe vivir junto al proyecto:

```text
020_Projects/
`-- C-UAS Indra/
    |-- README.md
    |-- Notes.md
    |-- Decisions.md
    |-- Papers/
    |-- Meetings/
    `-- Assets/
```

Las notas deben colocarse segun el tipo de compromiso:

- si tienen resultado y final claro, van a `020_Projects/`
- si son una responsabilidad continua, van a `030_Areas/`
- si son conocimiento reutilizable, van a `040_Resources/`
- si todavia no esta claro donde pertenecen, van a `000_Inbox/`
- si ya no estan activas, van a `090_Archive/`

Ejemplos:

```text
020_Projects/asignatura-dam-acceso-a-datos/
  -> asignatura activa con entregas, examenes, tareas y fechas

040_Resources/dam/acceso-a-datos/
  -> apuntes conservados como conocimiento reutilizable

030_Areas/Trabajo/
  -> responsabilidad continua de trabajo
```

Para apuntes de DAM que se quieren conservar a largo plazo, una estructura razonable es:

```text
040_Resources/
`-- dam/
    `-- acceso-a-datos/
        |-- README.md
        |-- jdbc.md
        |-- jpa.md
        |-- hibernate.md
        |-- xml-json.md
        `-- ejercicios.md
```

Si esos apuntes vienen de una asignatura todavia activa, pueden vivir temporalmente en `020_Projects/asignatura-dam-acceso-a-datos/` y moverse despues a `040_Resources/dam/acceso-a-datos/` cuando pasen a ser conocimiento de consulta.

## Reglas iniciales

- cada documento importante debe tener una identidad estable
- las rutas pueden cambiar, los identificadores no deberian
- los documentos deben ser legibles sin Spock
- el contenido debe seguir teniendo sentido en un editor Markdown normal
- los metadatos embebidos en Markdown deben mantenerse simples

## Frontmatter

Se puede usar frontmatter YAML para metadatos minimos y humanos del documento.

Ejemplo:

```yaml
---
id: note_java_streams
title: Java Streams
aliases:
  - Streams
tags:
  - java
status: permanent
created: 2026-06-28
---
```

El frontmatter no debe contener metadata tecnica interna como hashes, embeddings, OCR, fechas de indexacion, permisos internos o versiones tecnicas.

Regla:

```text
Si ayuda a entender el documento fuera de Spock, puede vivir en el Markdown.
Si sirve para que Spock funcione internamente, debe vivir en PostgreSQL.
```

El resumen detallado de tablas y metadata esta en [11_postgresql_y_metadata.md](11_postgresql_y_metadata.md).

## Obsidian

Obsidian puede usarse como editor visual principal del conocimiento, pero no debe convertirse en la fuente de verdad ni en una dependencia obligatoria para que Spock funcione.

La fuente de verdad del contenido sigue siendo `knowledge` como carpeta de archivos Markdown legibles.

Para facilitar que Spock abra, busque y modifique notas de forma controlada, conviene instalar al menos una herramienta CLI para operar con Obsidian o con el vault de Obsidian desde terminal.

Uso previsto:

- abrir notas o carpetas concretas desde Spock
- crear notas nuevas en ubicaciones conocidas
- buscar documentos dentro del vault
- ejecutar acciones repetibles sin depender de clicks en la interfaz
- permitir que agentes como OpenClaw trabajen sobre Markdown mediante comandos estables

Reglas:

- la CLI de Obsidian es una comodidad operativa, no la capa de negocio
- Spock Core no debe depender de plugins especificos de Obsidian para preservar el conocimiento
- cualquier accion que afecte a datos estructurados debe pasar por Spock Core
- los documentos deben seguir siendo editables y comprensibles sin Obsidian

## Problema clave: sincronizacion

Hay que disenar como se mantiene la consistencia entre:

- archivos Markdown
- registros de PostgreSQL
- indices de busqueda
- relaciones entre entidades

Opciones posibles:

- PostgreSQL como fuente de verdad de metadatos y Markdown como fuente de verdad de contenido
- Markdown como fuente de verdad y PostgreSQL como indice derivado
- modelo hibrido con reglas estrictas de propiedad por campo

La opcion recomendada inicialmente es el modelo hibrido con propiedad clara:

- contenido largo: Markdown
- estado estructurado y relaciones: PostgreSQL
- metadatos minimos de portabilidad: frontmatter

## Cuestiones abiertas

- formato exacto del frontmatter
- convencion de nombres de archivos
- estrategia de renombrado y movimiento de documentos
- mecanismo de deteccion de cambios manuales en Markdown
- busqueda full-text local
- generacion automatica de enlaces entre documentos
