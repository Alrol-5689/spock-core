# Spock Core

## Responsabilidad

Spock Core es el nucleo de logica de negocio de Spock.

Debe ser la frontera principal para crear, consultar, modificar y relacionar informacion.

## Regla de escritura

Ningun dashboard, agente o automatizacion importante debe escribir directamente en PostgreSQL.

Las escrituras deben pasar por la API para mantener validaciones, permisos, reglas de negocio y consistencia entre base de datos y Markdown.

Nombre recomendado:

```text
Spock Core
```

Spock es la experiencia completa y el asistente. Spock Core es el backend/API que protege el modelo de datos y las operaciones sobre conocimiento gestionado.

## Responsabilidades iniciales

- gestionar proyectos, tareas, documentos, personas, eventos y recordatorios
- resolver relaciones entre entidades y documentos Markdown
- crear documentos desde plantillas
- actualizar metadatos estructurados
- validar cambios propuestos por agentes
- exponer operaciones claras para dashboards y agentes
- preparar una capa futura de autenticacion y permisos

## Operaciones candidatas

Ejemplos conceptuales:

```text
POST   /projects
GET    /projects
GET    /projects/{id}
PATCH  /projects/{id}

POST   /documents
GET    /documents/{id}
PATCH  /documents/{id}

POST   /tasks
GET    /tasks
PATCH  /tasks/{id}

POST   /habits
GET    /habits
GET    /habits/{id}
PATCH  /habits/{id}
GET    /habits/{id}/versions
POST   /habits/{id}/versions
GET    /habits/{id}/occurrences
POST   /habits/{id}/occurrences
PATCH  /habit-occurrences/{id}

POST   /daily-logs
GET    /daily-logs
GET    /daily-logs/{id}
GET    /daily-logs/by-date/{date}
PATCH  /daily-logs/{id}

POST   /relations
GET    /entities/{id}/relations

POST   /areas
GET    /areas
PATCH  /areas/{id}

POST   /events
GET    /events
PATCH  /events/{id}

POST   /people
GET    /people
PATCH  /people/{id}

POST   /tags
GET    /tags
POST   /entity-tags
GET    /entity-tags?entityId={id}

POST   /reminders
GET    /reminders
GET    /reminders/{id}
PATCH  /reminders/{id}

POST   /pages
GET    /pages
GET    /pages/{id}
GET    /entities/{id}/page
PATCH  /pages/{id}

POST   /files
GET    /files
POST   /page-files
GET    /page-files?pageId={id}
POST   /indexed-directories
GET    /indexed-directories
PATCH  /indexed-directories/{id}

POST   /finance/accounts
GET    /finance/accounts
PATCH  /finance/accounts/{id}
POST   /finance/transactions
GET    /finance/transactions
PATCH  /finance/transactions/{id}
POST   /finance/reimbursements
POST   /finance/capital-snapshots
GET    /finance/capital-snapshots
```

Estos endpoints son orientativos. No deben implementarse sin revisar antes los casos de uso reales.

## Relacion con Markdown

El backend debe ser responsable de:

- crear archivos Markdown desde plantillas
- asignar rutas
- mantener referencias entre documentos y registros de base de datos
- detectar o reconciliar cambios hechos manualmente

## Relacion con agentes

Los agentes deben consumir la API igual que cualquier otro cliente.

Un agente puede proponer cambios, pero el backend debe aplicar las reglas que determinan si esos cambios son validos.

Los agentes pueden operar directamente sobre macOS para acciones de sistema operativo, como crear carpetas fuera de `knowledge`, mover archivos temporales o usar aplicaciones. Pero para datos gestionados por Spock deben usar Spock Core.

Ejemplos:

```text
crear tarea -> Spock Core
crear page Markdown en knowledge -> Spock Core
adjuntar archivo a page -> Spock Core
crear carpeta en Descargas -> herramienta de sistema operativo
rellenar formulario web -> herramienta de navegador/sistema
```

## Tecnologia

Tecnologia inicial elegida:

```text
Kotlin
Spring Boot
Spring Data JPA
Flyway
PostgreSQL
```

Criterios:

- buena integracion con PostgreSQL
- facilidad de mantenimiento
- ecosistema estable
- pruebas sencillas
- bajo acoplamiento a proveedores
- facilidad de despliegue local en macOS y Docker

## Organizacion interna

Spock Core empieza como un monolito modular.

Esto significa que hay una sola aplicacion backend desplegable, pero el codigo se separa por dominios para evitar que GTD, conocimiento, finanzas y automatizaciones se mezclen.

Estructura recomendada:

```text
com.alejandro.spock.core.gtd.task.model
com.alejandro.spock.core.gtd.task.repository
com.alejandro.spock.core.gtd.task.service
com.alejandro.spock.core.gtd.task.controller
com.alejandro.spock.core.gtd.task.dto

com.alejandro.spock.core.knowledge.page.model
com.alejandro.spock.core.knowledge.file.model
com.alejandro.spock.core.knowledge.indexing.model

com.alejandro.spock.core.finance.account.model
com.alejandro.spock.core.finance.transaction.model
com.alejandro.spock.core.finance.reimbursement.model
com.alejandro.spock.core.finance.capital.model
com.alejandro.spock.core.finance.report.service
com.alejandro.spock.core.finance.report.dto

com.alejandro.spock.core.reminder.model
com.alejandro.spock.core.reminder.repository
com.alejandro.spock.core.reminder.service
com.alejandro.spock.core.reminder.controller
com.alejandro.spock.core.reminder.dto

com.alejandro.spock.core.shared
```

Reglas:

- cada dominio debe concentrar su propia logica
- dentro de cada dominio, los paquetes se organizan por subdominio antes que por capa global
- `shared` debe ser pequeno y contener solo conceptos realmente transversales
- no crear microservicios mientras no exista una razon tecnica clara
- si un dominio crece mucho, se podra separar mas adelante porque ya tendra limites internos claros

## Cuestiones abiertas

- autenticacion local
- versionado de API
- permisos para agentes
- formato de errores
- estrategia de validacion
- logs y auditoria
- posible CLI `spockctl` para operaciones locales controladas
