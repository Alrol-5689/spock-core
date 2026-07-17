# Dominio Reminders

## Objetivo

El dominio `reminder` gestiona recordatorios asociados opcionalmente a una entidad de Spock.

Un recordatorio no pertenece solo a tareas. Puede apuntar a:

- tareas
- proyectos
- eventos
- personas
- paginas Markdown
- archivos
- cualquier otra entidad futura

La relacion se hace mediante `entity_id`.

Tambien puede existir un recordatorio independiente sin entidad asociada.

Ejemplo:

```text
Recuérdame beber agua a las 17:00
```

En ese caso:

```text
entity_id = null
title = "Beber agua"
```

## Regla principal

Spock Core debe ser la fuente de verdad de los recordatorios.

Telegram, Apple Reminders, Apple Calendar, email o una app de escritorio son canales de entrega o sincronizacion, no la fuente principal.

## Tabla `reminders`

Campos principales:

```text
id uuid primary key
entity_id uuid references entities(id)
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

## Estados

```text
PENDING
SENT
CANCELLED
FAILED
```

## Canales de entrega

```text
SPOCK_INTERNAL
TELEGRAM
DESKTOP_NOTIFICATION
APPLE_REMINDERS
APPLE_CALENDAR
EMAIL
```

## Funcionamiento recomendado

Un worker de Spock debe consultar periodicamente:

```text
status = PENDING
remind_at <= now()
```

Cuando encuentra recordatorios pendientes:

1. carga el recordatorio
2. resuelve la entidad asociada si existe
3. entrega el aviso por el canal configurado
4. marca `sent_at`
5. cambia `status` a `SENT`

Si falla:

1. guarda `last_error`
2. cambia `status` a `FAILED` o deja `PENDING` para reintento segun la regla futura

## Texto mostrado

Si el recordatorio tiene `title`, Spock usa ese titulo.

Si `title` es null y existe entidad asociada, Spock usa `entity.title`.

Si `title` es null y no existe entidad asociada, la API debe rechazar la creacion del recordatorio porque no habria texto util que mostrar.

Regla recomendada:

```text
title != null OR entity_id != null
```

## Frecuencia inicial

Para la primera version basta con revisar cada minuto.

```text
cada 60 segundos
```

Es suficientemente preciso para recordatorios personales y evita complejidad prematura.

Mas adelante se puede cambiar a:

- cola de trabajos
- scheduler persistente
- Quartz
- notificaciones push
- integracion con una app local de escritorio

## Telegram, app o notificacion local

Opciones:

```text
Telegram
```

Ventajas:

- funciona tambien desde el movil
- encaja con OpenClaw
- no requiere app nativa al inicio

Inconvenientes:

- depende de Telegram
- requiere bot y configuracion

```text
Mini app de macOS
```

Ventajas:

- permite notificaciones locales nativas
- puede integrarse mejor con el Mac
- no depende de Telegram

Inconvenientes:

- requiere construir y mantener una app/helper
- en el Mac mini servidor puede no ser tan util si no estas delante

```text
Spock interno
```

Ventajas:

- simple
- no depende de proveedores externos

Inconvenientes:

- solo sirve si tienes una interfaz abierta o un dashboard que muestre pendientes

## Recomendacion inicial

Empezar con:

```text
Spock Core reminders
worker local cada 60 segundos
delivery_channel = TELEGRAM
```

Despues anadir:

```text
DESKTOP_NOTIFICATION
```

Y dejar:

```text
APPLE_REMINDERS
APPLE_CALENDAR
```

como integraciones opcionales futuras.

## Relacion con app movil

Una app movil puede servir para:

- crear recordatorios
- consultar recordatorios pendientes
- recibir notificaciones push en una fase futura

Pero una app movil no sustituye al worker mientras Spock viva en el Mac.

El worker debe ejecutarse en el entorno donde vive Spock Core, inicialmente el Mac encendido y mas adelante el Mac mini servidor.
