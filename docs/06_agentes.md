# Agentes

## Principio

Los agentes son componentes reemplazables de Spock.

No son el sistema operativo personal. No son la fuente de verdad. No deben contener la logica central del proyecto.

## OpenClaw

OpenClaw sera el primer motor/agente operativo dentro del ecosistema Spock.

Responsabilidades previstas:

- consultar informacion
- crear informacion
- modificar documentos
- automatizar tareas
- comunicarse mediante Telegram
- comunicarse mediante la interfaz web
- operar sobre macOS cuando la accion sea externa al estado gestionado por Spock

Estado temporal 2026-07-02:

- OpenClaw esta instalado en un Ubuntu Server de la red local
- Telegram esta configurado como canal inicial
- el acceso por DM debe estar restringido por allowlist
- Codex Hosted Search es el proveedor inicial de busqueda web
- los detalles operativos viven en [15_openclaw_ubuntu_server.md](15_openclaw_ubuntu_server.md)

## Limite arquitectonico

OpenClaw no debe escribir directamente en la base de datos.

Debe operar mediante Spock Core para cualquier accion que modifique datos gestionados por Spock.

Esto evita que la logica de negocio quede repartida entre agentes y permite reemplazar OpenClaw en el futuro.

La regla correcta no es que OpenClaw solo pueda hacer cosas mediante API.

La regla correcta es:

```text
OpenClaw puede actuar sobre el sistema operativo.
OpenClaw no puede saltarse Spock Core para modificar el estado estructurado de Spock.
```

## Tipos de operaciones

Un agente puede:

- leer datos estructurados
- leer documentos Markdown
- proponer cambios
- crear tareas o recordatorios
- generar borradores
- ejecutar automatizaciones aprobadas
- crear carpetas fuera de `knowledge`
- mover archivos externos a Spock
- usar navegador, Finder u otras aplicaciones

Un agente no deberia:

- saltarse validaciones del backend
- modificar esquemas de base de datos
- depender de rutas internas como interfaz publica
- convertirse en el unico lugar donde viven reglas importantes
- modificar `knowledge` gestionado sin sincronizacion o herramienta oficial de Spock

## Permisos futuros

Spock deberia poder asignar permisos por agente.

Ejemplos:

- solo lectura
- lectura y creacion
- lectura y modificacion
- acciones peligrosas con confirmacion
- acceso limitado a ciertas areas de conocimiento

## Riesgos

- que un agente acumule demasiada responsabilidad
- que automatizaciones modifiquen datos sin trazabilidad
- que los prompts se conviertan en logica de negocio oculta
- que se mezclen comandos de usuario, reglas del sistema y decisiones persistentes sin auditoria

## Decision inicial

Tratar OpenClaw como cliente privilegiado de Spock Core y como agente operativo sobre macOS, no como backend.

Las reglas importantes se documentaran y se implementaran en Spock, no en prompts ni scripts aislados.
