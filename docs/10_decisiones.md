# Decisiones arquitectonicas

Este documento es un indice de decisiones importantes.

Para decisiones grandes, se deberian crear documentos separados en una carpeta futura `docs/decisions`.

## Decisiones iniciales

### 2026-06-26: Spock es el sistema, OpenClaw es un agente

Estado: aceptada

Spock sera el sistema operativo personal basado en IA. OpenClaw sera un agente dentro de Spock, no el propietario de la arquitectura.

Consecuencias:

- OpenClaw debe comunicarse mediante la API
- la logica de negocio vive en el backend
- otros agentes podran reemplazar o complementar OpenClaw

### 2026-06-26: Markdown para conocimiento

Estado: aceptada

El conocimiento vivira en archivos Markdown dentro de `knowledge`.

Consecuencias:

- los documentos largos no se guardan en PostgreSQL
- el conocimiento sigue siendo legible sin Spock
- hay que disenar sincronizacion entre Markdown y metadatos estructurados

### 2026-07-11: Estructura humana del vault de conocimiento

Estado: aceptada

La estructura fisica de `knowledge` no debe copiar la estructura interna de la API. La API puede mantener dominios tecnicos como `gtd`, `knowledge`, `finance` o `reminders`, pero el vault se organizara para lectura y mantenimiento humano.

Convencion inicial:

- `000_Inbox`: capturas sin procesar
- `010_GTD`: indices operativos de tareas, eventos, proyectos, esperas y revisiones
- `020_Projects`: compromisos con resultado y final claro
- `030_Areas`: responsabilidades continuas
- `040_Resources`: conocimiento reutilizable
- `090_Archive`: material cerrado o inactivo
- `100_Templates`: plantillas

Consecuencias:

- `010_GTD` no es el almacen principal de conocimiento, sino el panel de control
- tareas, eventos y proyectos pueden tener paginas Markdown, pero sus adjuntos, papers y notas largas viven normalmente junto al proyecto, area o recurso correspondiente
- apuntes reutilizables, como DAM/Acceso a Datos, pueden vivir en `040_Resources/dam/acceso-a-datos/`
- una asignatura activa puede vivir temporalmente en `020_Projects/asignatura-dam-acceso-a-datos/` y migrar despues a `040_Resources/`

### 2026-06-26: PostgreSQL para datos estructurados

Estado: propuesta

PostgreSQL sera la base de datos inicial para datos estructurados.

Consecuencias:

- buena estabilidad y portabilidad
- permite relaciones y consultas complejas
- requiere definir migraciones y backups

### 2026-06-26: API como frontera de escritura

Estado: aceptada

Los agentes y dashboards no escribiran directamente en PostgreSQL.

Consecuencias:

- el backend concentra validaciones y reglas
- se reduce el acoplamiento
- se evita que automatizaciones creen estados inconsistentes

### 2026-06-28: Spock Core como nombre del backend/API

Estado: aceptada

Spock sera el nombre de la experiencia completa y del asistente personal. El backend/API se llamara Spock Core.

Consecuencias:

- Spock puede ser la identidad con la que el usuario conversa
- Spock Core concentra logica de negocio, validaciones y acceso a datos gestionados
- OpenClaw puede ser motor/agente operativo inicial sin confundirse con el sistema
- la base de datos local mantiene el nombre `spock`
- una CLI futura podria llamarse `spockctl`

### 2026-06-28: Spock Core como monolito modular

Estado: aceptada

Spock Core empieza como una sola API, organizada internamente por dominios.

Dominios iniciales:

- `gtd`
- `knowledge`
- `finance`
- `shared`

Consecuencias:

- se evita la complejidad prematura de microservicios
- cada dominio tiene paquetes propios de `model`, `repository`, `service` y `controller`
- si un dominio crece demasiado, tendra limites internos claros para separarse en el futuro

### 2026-06-28: Dominio Finance integrado en Spock Core

Estado: aceptada

La economia personal vivira inicialmente dentro de Spock Core como dominio `finance`, no como una API independiente.

Consecuencias:

- una sola base de datos de Spock
- modelos financieros en Kotlin/JPA
- migraciones con Flyway
- cantidades monetarias con `BigDecimal`
- sin copiar el modelo de usuarios del proyecto antiguo mientras Spock sea single-user

### 2026-06-26: macOS como infraestructura inicial

Estado: aceptada

Spock se desarrollara inicialmente en un Mac mini M1 con macOS.

Consecuencias:

- no se introduce Linux prematuramente
- Docker se usara para servicios como PostgreSQL
- hay que considerar arranque automatico y operacion local en macOS

### 2026-07-02: Ubuntu server como runtime temporal de OpenClaw

Estado: aceptada temporalmente

OpenClaw se ejecutara temporalmente en un portatil Ubuntu Server dentro de la red local.

Consecuencias:

- OpenClaw puede estar disponible 24/7 sin depender del Mac
- el canal Telegram se opera desde Ubuntu y debe restringirse por allowlist
- el vault de Obsidian debe sincronizarse entre Mac y Ubuntu, no asumirse como una ruta compartida unica
- Spock Core puede moverse a Ubuntu cuando se necesite backend permanente
- la decision no cambia que OpenClaw sea un agente y Spock Core la frontera de escritura estructurada
- la documentacion operativa vive en [15_openclaw_ubuntu_server.md](15_openclaw_ubuntu_server.md)

## Decisiones pendientes

- formato final del frontmatter
- estrategia de IDs
- convencion detallada de nombres de archivos dentro de la estructura inicial de `knowledge`
- estrategia de busqueda
- sistema de permisos para agentes
- mecanismo de backup y cifrado
