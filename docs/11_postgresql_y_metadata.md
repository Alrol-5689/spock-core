# PostgreSQL y metadata de documentos

## Estado

Este documento resume el modelo recomendado inicial para Spock.

La arquitectura parte de una idea similar a Notion:

> Cada registro importante puede tener una pagina, pero no todos los registros necesitan tenerla.

## Principios

- PostgreSQL guarda la informacion estructurada y operativa.
- Markdown guarda conocimiento editable y portable.
- Los archivos binarios o externos se guardan como ficheros y se registran en PostgreSQL.
- La metadata visible en archivos Markdown debe ser minima y humana.
- La metadata tecnica no debe ensuciar los documentos.

## Tablas principales

### `entities`

Representa cualquier objeto conceptual importante de Spock.

Ejemplos:

- tarea
- proyecto
- evento
- persona
- area
- recurso
- nota

Campos recomendados:

```text
id uuid primary key
entity_type text not null
title text not null
slug text
summary text
status text
created_at timestamptz not null
updated_at timestamptz not null
archived_at timestamptz
```

Uso:

- dar una identidad global a todo lo relacionable
- permitir busqueda unificada
- permitir relaciones entre tipos distintos
- servir como base comun para tablas especificas

Notas:

- `path` no debe vivir aqui, porque no todas las entidades tienen archivo.
- `hash`, OCR, embeddings e indexacion no deben vivir aqui.
- en una version multiusuario futura, probablemente se anadiria `workspace_id`.

### `tasks`

Datos especificos de tareas.

Campos recomendados:

```text
id uuid primary key references entities(id) on delete cascade
status text not null
priority text
due_at timestamptz
scheduled_at timestamptz
completed_at timestamptz
created_at timestamptz not null
updated_at timestamptz not null
```

Uso:

- representar acciones GTD
- permitir filtros por estado, prioridad y fecha
- mantener tareas simples sin obligarlas a tener pagina Markdown

### `projects`

Datos especificos de proyectos.

Campos recomendados:

```text
id uuid primary key references entities(id) on delete cascade
status text not null
started_at date
due_date date
ended_at date
created_at timestamptz not null
updated_at timestamptz not null
```

Uso:

- agrupar tareas, notas, eventos y recursos
- representar resultados deseados con varias acciones asociadas
- permitir fechas objetivo o limite del resultado esperado
- permitir calculos agregados como porcentaje de avance a partir de tareas relacionadas

### `habits`

Datos especificos de habitos.

Un habito representa un comportamiento recurrente y medible. No debe confundirse con una tarea puntual ni con el proyecto al que puede contribuir.

Campos recomendados:

```text
id uuid primary key
project_id uuid references projects(id)
name text not null
description text
value_type text not null
unit text
active boolean not null
created_at timestamptz not null
updated_at timestamptz not null
archived_at timestamptz
```

Uso:

- representar sistemas repetidos, como correr, registrar peso o planificar comidas
- asociar habitos a proyectos cuando ayuden a avanzar un objetivo grande
- distinguir habitos booleanos de mediciones numericas, conteos, duraciones o texto
- permitir activar o desactivar globalmente un habito sin borrar su historia
- mantener una identidad estable aunque cambien sus reglas de frecuencia

Notas:

- `project_id` es nullable porque puede haber habitos sin proyecto concreto.
- `value_type` puede ser `BOOLEAN`, `NUMBER`, `COUNT`, `DURATION` o `TEXT`.
- `unit` es nullable y guarda una unidad humana como `kg`, `min`, `pasos` o `paginas`.
- `active = false` desactiva el habito hacia adelante, pero no borra registros historicos.
- `archived_at` permite retirar habitos del uso normal sin perder estadisticas.

### `habit_versions`

Versiones de reglas de un habito.

Cada version describe las reglas vigentes durante una etapa concreta. Esto evita romper estadisticas historicas cuando cambian la frecuencia, los dias o el objetivo del habito.

Campos recomendados:

```text
id uuid primary key
habit_id uuid not null references habits(id) on delete cascade
starts_on date not null
ends_on date
frequency_type text not null
target_count integer
weekdays text
active boolean not null
created_at timestamptz not null
updated_at timestamptz not null
```

Uso:

- definir desde que fecha aplica una regla de habito
- cerrar reglas antiguas con `ends_on`
- crear nuevas reglas con `starts_on` cuando cambie la frecuencia o los dias
- preservar el significado de las ocurrencias antiguas

Reglas:

- los cambios reales de frecuencia, dias u objetivo crean una nueva `habit_version`
- la version antigua se cierra con `ends_on`
- la nueva version empieza con `starts_on`
- las correcciones de errores de configuracion si pueden editar la version existente
- no deberia haber dos versiones activas solapadas para el mismo habito

Ejemplos de `frequency_type`:

```text
daily
weekly_count
weekly_weekdays
monthly_count
custom
```

`weekdays` puede empezar como texto o array simple, por ejemplo `MON,WED`, y normalizarse mas adelante si las consultas lo requieren.

### `habit_occurrences`

Instancias concretas de un habito en una fecha.

Cada dia que toca evaluar un habito debe materializarse como una ocurrencia. La ocurrencia apunta tanto al habito como a la version que genero esa expectativa.

Campos recomendados:

```text
id uuid primary key
habit_id uuid not null references habits(id) on delete cascade
habit_version_id uuid not null references habit_versions(id)
due_date date not null
status text not null
disabled boolean not null
skipped_reason text
notes text
numeric_value numeric
count_value integer
duration_seconds integer
text_value text
recorded_at timestamptz
created_at timestamptz not null
updated_at timestamptz not null
```

Estados recomendados:

```text
pending   pendiente
done      hecho
missed    no hecho
skipped   justificado, no cuenta como fallo
disabled  no evaluable, no deberia contar
```

Uso:

- registrar el resultado de cada dia concreto
- registrar valores tipados cuando el habito no sea booleano, por ejemplo peso, pasos o minutos
- calcular rachas, cumplimiento y desviaciones
- conservar el contexto exacto de la regla vigente cuando se genero la ocurrencia
- diferenciar entre no hacer algo y justificar que no era evaluable

Reglas:

- para fiebre, viaje, enfermedad u otra causa justificada, usar preferiblemente `skipped` en vez de borrar el registro
- `disabled = true` representa una ocurrencia que no deberia evaluarse
- `status = disabled` puede usarse cuando se quiera expresar el estado directamente en el mismo campo de estado
- solo debe rellenarse un campo de valor por ocurrencia
- el backend debe evitar duplicados para la misma combinacion de `habit_id`, `habit_version_id` y `due_date`
- las ocurrencias historicas no deben recalcularse destructivamente cuando cambia una version futura

### `daily_logs`

Registro estructurado de un dia, pensado para asociar una pagina Markdown diaria tipo diario.

Campos recomendados:

```text
id uuid primary key references entities(id) on delete cascade
log_date date not null unique
```

Uso:

- crear o encontrar rapidamente la pagina diaria de trabajo
- dar identidad propia al dia para enlazar notas, tareas, eventos y recursos
- servir como contenedor narrativo sin convertir el Markdown en fuente unica de datos estructurados

Notas:

- la pagina diaria se asocia mediante `pages.entity_id = daily_logs.id`
- los habitos se relacionan con el dia por `habit_occurrences.due_date = daily_logs.log_date`
- no hace falta guardar `daily_log_id` en cada ocurrencia mientras la fecha baste para las consultas

## Modelo conceptual de habitos

La diferencia entre proyecto, tarea, habito y ocurrencia es central:

```text
Proyecto = objetivo grande
Tareas = pasos puntuales
Habitos = sistemas repetidos
Habit versions = reglas vigentes en una etapa
Habit occurrences = dias concretos que toca hacerlo
Habit logs/status = resultado de cada dia
Daily logs = contenedor narrativo y pagina del dia
```

Definiciones:

- Proyecto: objetivo grande o resultado deseado, normalmente compuesto por muchas acciones y recursos.
- Tarea: accion puntual que se puede completar una vez.
- Habito: comportamiento recurrente medible que se repite bajo unas reglas.
- Occurrence/registro: instancia concreta de un habito en una fecha.
- Daily log: registro del dia y pagina Markdown asociada, util para diario y captura rapida.

Ejemplo:

```text
Proyecto: Bajar a 70 kg

Habitos asociados:
- Correr lunes y miercoles
- Pesas 3 dias por semana
- Planificar comidas los domingos
- Registrar peso semanalmente
- Cumplir dieta diariamente
```

## Estadisticas de habitos

Las estadisticas deben calcularse sobre ocurrencias evaluables.

```text
Evaluables = pending + done + missed
No evaluables = skipped + disabled

Cumplimiento = done / evaluables
```

Ejemplo:

```text
Si una semana tocaba correr 3 dias:
- 2 ocurrencias se marcan como done
- 1 ocurrencia se marca como skipped por fiebre

Cumplimiento evaluable = 2 / 2 = 100%
Planificacion original = 2 / 3 completados, 1 justificado
```

Esto permite distinguir entre disciplina real y circunstancias justificadas sin borrar historia.

## Cambios de habitos

Los habitos no deben editarse destructivamente cuando cambia su regla real.

Ejemplo de cambio de frecuencia:

```text
Salir a correr pasa de 3 dias por semana a 4 dias por semana.
```

Regla:

- no se edita destructivamente el habito
- se cierra la version antigua con `ends_on`
- se crea una nueva `habit_version` con `starts_on`
- las ocurrencias antiguas siguen apuntando a la version anterior

Ejemplo de cambio de dia:

```text
Planificar comidas pasa de domingo a sabado.
```

Regla:

- si es un cambio real desde ahora, crear nueva version
- si fue un error al configurarlo, editar la version existente

### `areas`

Datos especificos de areas de responsabilidad o interes.

Campos recomendados:

```text
id uuid primary key references entities(id) on delete cascade
type text not null
status text not null
created_at timestamptz not null
updated_at timestamptz not null
```

Uso:

- representar areas estables y poco numerosas
- clasificar areas por tipo general, como trabajo, educacion, finanzas o salud
- agrupar proyectos, tareas, eventos y notas por contexto vital
- permitir dashboards por area

Ejemplos:

```text
Indra        type = WORK
UEM DAM      type = EDUCATION
Inversiones  type = FINANCE
Salud        type = HEALTH
Spock        type = SYSTEM
```

Notas:

- `areas` merece tabla propia porque es una categoria central de GTD, no una simple etiqueta.
- `type` no sustituye a `Area`; solo agrupa areas para filtros y dashboards.
- una entidad puede pertenecer a un area mediante `entity_relations` con `relation_type = belongs_to_area`.
- si en el futuro casi todas las entidades pertenecen exactamente a un area, se podria evaluar una columna `area_id`, pero no es necesario al inicio.

### `events`

Datos especificos de eventos.

Campos recomendados:

```text
id uuid primary key references entities(id) on delete cascade
starts_at timestamptz not null
ends_at timestamptz
event_type text not null
all_day boolean not null
location text
created_at timestamptz not null
updated_at timestamptz not null
```

Uso:

- representar eventos de calendario
- clasificar eventos por tipo funcional para filtros, color, icono, dashboards y automatizaciones
- distinguir eventos de dia completo frente a eventos con hora concreta
- permitir asociar una pagina opcional al evento
- permitir relacionar eventos con personas, proyectos o areas

### `people`

Datos especificos de personas.

Campos recomendados:

```text
id uuid primary key references entities(id) on delete cascade
display_name text not null
email text
phone text
created_at timestamptz not null
updated_at timestamptz not null
```

Uso:

- representar contactos relevantes
- relacionar personas con proyectos, eventos, reuniones, notas o tareas

### `pages`

Representa una pagina Markdown asociada opcionalmente a una entidad.

Campos recomendados:

```text
id uuid primary key
entity_id uuid not null references entities(id) on delete cascade
title text not null
markdown_path text not null unique
created_at timestamptz not null
updated_at timestamptz not null
last_synced_at timestamptz
```

Uso:

- dar a cualquier entidad una pagina editable tipo Notion
- enlazar el registro estructurado con su documento Markdown
- permitir que una tarea, proyecto, evento o persona tenga una pagina cuando lo necesite

Regla inicial:

- una entidad puede tener cero o una pagina principal
- si mas adelante hacen falta varias paginas por entidad, se puede anadir `page_type` o permitir multiples filas por `entity_id`

### `files`

Representa un archivo fisico gestionado por Spock.

No significa necesariamente que el archivo viva dentro de `knowledge`.

Spock distingue entre:

```text
knowledge = paginas Markdown gestionadas por Spock
files = archivos conocidos por Spock, vivan donde vivan
```

Ejemplos:

- PDF
- Excel
- imagen
- documento Pages
- Word
- ZIP
- CSV
- audio

Campos recomendados:

```text
id uuid primary key
file_path text not null unique
display_name text
original_filename text
file_kind text not null
mime_type text
size_bytes bigint
checksum_sha256 text
storage_mode text not null
last_seen_at timestamptz
missing_at timestamptz
created_at timestamptz not null
updated_at timestamptz not null
```

Uso:

- registrar archivos que viven en `knowledge` o en una carpeta gestionada
- registrar archivos que viven fuera de `knowledge`, como Documents, Downloads, Dropbox, OneDrive o Desktop
- identificar archivos aunque cambie su nombre visible
- permitir adjuntarlos a paginas
- conservar informacion necesaria para backups, sincronizacion e indexacion

Notas:

- el archivo no contiene necesariamente metadata propia editable
- PostgreSQL es la fuente de verdad para metadata operativa
- `checksum_sha256` permite reconocer un archivo movido o renombrado
- `last_seen_at` indica cuando Spock vio el archivo por ultima vez
- `missing_at` indica desde cuando la ruta conocida dejo de existir
- `storage_mode` diferencia archivos gestionados, importados, referenciados o sincronizados externamente

Valores iniciales de `storage_mode`:

```text
MANAGED
REFERENCED
IMPORTED
EXTERNAL_SYNCED
```

### `indexed_directories`

Representa carpetas conocidas que Spock puede escanear para localizar archivos movidos, renombrados o nuevos.

Campos recomendados:

```text
id uuid primary key
path text not null unique
enabled boolean not null
recursive boolean not null
last_scanned_at timestamptz
created_at timestamptz not null
updated_at timestamptz not null
```

Uso:

- saber que carpetas puede escanear Spock
- permitir que los archivos vivan repartidos por macOS sin obligar a moverlos a `knowledge`
- reconstruir rutas usando `checksum_sha256` y `size_bytes`
- pausar carpetas sin borrar su configuracion

Ejemplos:

```text
~/Documents
~/Downloads
~/Desktop
~/OneDrive
~/Dropbox
```

### `page_files`

Relaciona paginas con archivos adjuntos.

Campos recomendados:

```text
id uuid primary key
page_id uuid not null references pages(id) on delete cascade
file_id uuid not null references files(id) on delete cascade
created_at timestamptz not null
unique (page_id, file_id)
```

Uso:

- permitir que una pagina tenga varios archivos
- permitir que un archivo pueda aparecer en varias paginas si hace falta

Ejemplo:

```text
Evento "Cumpleanos de Laura"
  -> page "Cumpleanos de Laura"
  -> files: fotos.zip, invitacion.pdf
```

### `entity_relations`

Relaciona entidades entre si.

Esta tabla permite relaciones casi todo con todo entre objetos vivos del sistema. Es una pieza central de Spock porque el sistema no es solo GTD rigido: tambien es una base de conocimiento operativa.

Campos recomendados:

```text
id uuid primary key
source_entity_id uuid not null references entities(id) on delete cascade
target_entity_id uuid not null references entities(id) on delete cascade
relation_type text not null
created_at timestamptz not null
```

Uso:

- relacionar tareas con proyectos
- relacionar tareas con subtareas
- relacionar tareas con eventos
- relacionar tareas con notas
- relacionar tareas con personas
- relacionar notas con areas
- relacionar eventos con personas
- relacionar recursos con proyectos
- relacionar proyectos, tareas, eventos y notas con areas

Ejemplos:

```text
Tarea -> belongs_to -> Proyecto
Subtarea -> subtask_of -> Tarea
Tarea -> related_to_event -> Evento
Tarea -> related_to_note -> Nota
Tarea -> involves_person -> Persona
Proyecto -> belongs_to_area -> Area
Tarea -> belongs_to_area -> Area
Evento -> belongs_to_area -> Area
Evento -> involves -> Persona
Nota -> part_of -> Area
Proyecto -> related_to -> Recurso
```

Regla:

- las relaciones importantes son entre entidades, no entre rutas de archivos
- los adjuntos fisicos no se modelan aqui, sino en `page_files`

Separacion recomendada:

```text
entity_relations = relaciones conceptuales entre entidades
page_files = archivos adjuntos a paginas
```

Ejemplo:

```text
Tarea "Comprar regalos"
  -> related_to_event -> Evento "Cumpleanos de Pepito"
  -> involves_person -> Persona "Pepito"
  -> belongs_to_area -> Area "Familia"
  -> related_to_note -> Nota "Ideas de regalos"
```

En este ejemplo, la nota "Ideas de regalos" puede tener su propia pagina Markdown y archivos adjuntos. La tarea se relaciona con la nota como concepto, no con sus archivos internos.

Relaciones recomendadas para GTD:

```text
task -> belongs_to_project -> project
task -> subtask_of -> task
task -> related_to_event -> event
task -> related_to_note -> note
task -> involves_person -> person
project -> belongs_to_area -> area
task -> belongs_to_area -> area
event -> belongs_to_area -> area
note -> belongs_to_area -> area
```

Esto permite mantener el modelo flexible y, a la vez, calcular dashboards. Por ejemplo, el porcentaje de avance de un proyecto puede calcularse consultando las tareas relacionadas con `belongs_to_project` y contando cuantas estan completadas.

Ejemplo de progreso de proyecto:

```text
Proyecto "Curso Java"
  -> Tarea "Ver modulo Streams"      status = completed
  -> Tarea "Hacer ejercicios"        status = open
  -> Tarea "Subir certificado"       status = open

Progreso = tareas completadas / tareas totales = 1 / 3 = 33%
```

Notas:

- no se recomienda duplicar este vinculo con una tabla `project_tasks` al inicio
- si el progreso necesita reglas mas complejas, como pesos, estimaciones o tareas excluidas, se puede anadir metadata a `entity_relations` o crear una tabla especifica mas adelante
- para subtareas, se recomienda empezar con `entity_relations` y `relation_type = subtask_of`
- los tipos de relacion deberian estar controlados por el backend para evitar que la tabla se convierta en un cajon desordenado
- el backend deberia validar combinaciones razonables de tipos, por ejemplo `subtask_of` solo entre tareas

### `tags`

Catalogo de etiquetas.

Campos recomendados:

```text
id uuid primary key
name text not null unique
created_at timestamptz not null
```

Uso:

- normalizar etiquetas
- evitar duplicados como `java`, `Java`, `JAVA`
- permitir filtros estables

### `entity_tags`

Relaciona entidades con etiquetas.

Campos recomendados:

```text
id uuid primary key
entity_id uuid not null references entities(id) on delete cascade
tag_id uuid not null references tags(id) on delete cascade
created_at timestamptz not null
unique (entity_id, tag_id)
```

Uso:

- etiquetar proyectos, tareas, eventos, notas, personas o recursos
- permitir busqueda y filtros transversales

### `file_indexes`

Guarda estado tecnico de indexacion de archivos.

Campos recomendados:

```text
file_id uuid primary key references files(id) on delete cascade
indexed_at timestamptz
last_synced_at timestamptz
content_hash text
ocr_status text
ocr_text_path text
embedding_status text
embedding_id text
index_version integer
error_message text
```

Uso:

- saber si un archivo fue indexado
- saber si necesita OCR
- saber si tiene embeddings
- evitar reindexar archivos que no han cambiado
- mantener fuera del documento la metadata tecnica

Notas:

- `embedding_id`, OCR, hash y fechas de indexacion son internos de Spock
- no deben aparecer en el frontmatter Markdown

### `reminders`

Representa recordatorios asociados a cualquier entidad de Spock.

Campos recomendados:

```text
id uuid primary key
entity_id uuid references entities(id) on delete cascade
remind_at timestamptz not null
title text
message text
status text not null
delivery_channel text not null
external_provider text
external_id text
created_at timestamptz not null
updated_at timestamptz not null
sent_at timestamptz
cancelled_at timestamptz
last_error text
```

Uso:

- crear recordatorios para tareas, proyectos, eventos, notas o archivos
- crear recordatorios independientes sin entidad asociada
- entregar avisos por Telegram, notificacion local u otro canal futuro
- sincronizar opcionalmente con Apple Reminders o Apple Calendar
- mantener Spock Core como fuente de verdad aunque existan proveedores externos

Regla:

```text
title != null OR entity_id != null
```

Si `title` es null, Spock puede usar el titulo de la entidad asociada.

## Relacion entre entidades, paginas y archivos

Modelo recomendado:

```text
entities
  -> tasks / projects / events / people / ...
  -> optional pages
       -> page_files
            -> files
```

Ejemplo de tarea simple:

```text
entities: Comprar leche
tasks: status = open
pages: no existe
files: no existen
```

Ejemplo de tarea con documentos adjuntos:

```text
entities: Subir documentos a plataforma universidad
tasks: status = open, due_at = 2026-07-03
pages: knowledge/GTD/Tasks.md o knowledge/Projects/matricula-universidad/README.md
files: dni.pdf, justificante-matricula.pdf, formulario-firmado.pdf
```

En este caso, la tarea no se relaciona directamente con los archivos. La tarea tiene una pagina y los archivos se adjuntan a esa pagina mediante `page_files`.

Cuando el usuario abre la tarea, Spock debe cargar:

```text
entities + tasks
  -> pages
      -> page_files
          -> files
```

Ejemplo de proyecto con pagina:

```text
entities: Spock
projects: status = active
pages: knowledge/Projects/spock/README.md
files: ninguno o varios adjuntos
```

Ejemplo de evento con fotos:

```text
entities: Cumpleanos de Laura
events: starts_at = ...
pages: knowledge/Areas/Familia/Events/2026-xx-xx-cumpleanos-laura.md
files: fotos.zip, invitacion.pdf
```

## Metadata visible en Markdown

Los archivos `.md` pueden tener frontmatter YAML.

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

Esta metadata debe ser:

- humana
- portable
- util aunque Spock desaparezca
- pequena
- facil de editar a mano

Campos adecuados:

```text
id
title
aliases
tags
status
created
updated
```

Campos que no deben ir en frontmatter:

```text
embedding_id
ocr_status
checksum_sha256
indexed_at
last_synced_at
permissions
internal_version
database_primary_key tecnico si no es portable
```

Regla:

> Si ayuda a entender el documento fuera de Spock, puede vivir en el Markdown. Si sirve para que Spock funcione internamente, debe vivir en PostgreSQL.

## Metadata por tipo de archivo

### Markdown `.md`

Puede tener metadata visible mediante frontmatter YAML.

Recomendado para:

- notas
- paginas de proyectos
- paginas de tareas complejas
- paginas de eventos
- documentacion personal
- recursos de conocimiento

Fuente de verdad:

- contenido largo: archivo Markdown
- metadata portable minima: frontmatter
- estado estructurado y relaciones: PostgreSQL
- indexacion tecnica: PostgreSQL

### Excel `.xlsx`, CSV `.csv`, Numbers

No se recomienda depender de metadata interna del archivo.

Puede tener metadata registrada en PostgreSQL:

```text
file_kind
mime_type
original_filename
checksum_sha256
size_bytes
metadata tecnica de indexacion
```

Si se necesita contexto humano, lo ideal es adjuntarlo a una pagina Markdown que explique el archivo.

Ejemplo:

```text
knowledge/investments/cartera-2026.md
  -> adjunto: knowledge/assets/investments/cartera-2026.xlsx
```

### PDF `.pdf`

Puede tener metadata interna, pero no debe ser fuente de verdad.

Spock deberia guardar en PostgreSQL:

```text
file_path
mime_type
checksum_sha256
ocr_status
ocr_text_path
indexed_at
```

Si el PDF necesita explicacion o relaciones, debe estar adjunto a una pagina Markdown.

### Apple Pages `.pages`, Word `.docx`

Pueden tener propiedades internas, pero no son una base fiable para metadata de Spock.

Spock debe tratarlos como archivos adjuntos registrados en `files`.

Si son importantes, deberian tener una pagina Markdown asociada con contexto humano.

### Imagenes, audio, video, ZIP

No deben depender de metadata interna como fuente de verdad.

Spock debe guardar metadata tecnica en PostgreSQL y, si hace falta, una descripcion humana en una pagina Markdown.

## Metadata de macOS

Los metadatos de Finder, etiquetas de macOS, comentarios y atributos extendidos pueden usarse como comodidad local.

No deben ser fuente de verdad porque:

- no son portables de forma fiable
- pueden perderse al copiar, comprimir o sincronizar
- no funcionan igual fuera de macOS
- no son adecuados para relaciones complejas
- no se versionan bien

Uso recomendado:

```text
macOS tags/comments = ayuda visual opcional
PostgreSQL = fuente de verdad operativa
Markdown frontmatter = metadata portable minima
```

## Recomendacion inicial

Para Spock v1:

- usar `entities` como identidad comun
- usar tablas especificas para tareas, proyectos, eventos y personas
- permitir que cualquier entidad tenga una pagina Markdown opcional
- adjuntar archivos a paginas mediante `page_files`
- relacionar conceptos mediante `entity_relations`
- mantener metadata tecnica fuera de los documentos
- usar frontmatter solo en Markdown y solo para metadata humana y portable
