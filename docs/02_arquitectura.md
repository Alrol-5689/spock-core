# Arquitectura general

## Vision de capas

Spock se organiza en capas con responsabilidades separadas:

```text
Interfaces
  - dashboards
  - web
  - Telegram
  - CLI futura

Agentes
  - OpenClaw
  - otros agentes IA futuros

Backend
  - Spock Core
  - API principal
  - logica de negocio
  - permisos y validaciones
  - orquestacion de operaciones

Persistencia
  - PostgreSQL para datos estructurados
  - Markdown para conocimiento
  - filesystem para documentos y plantillas

Infraestructura
  - Docker
  - backups
  - scripts
  - despliegue local
```

## Regla principal de dependencia

Las dependencias deben apuntar hacia el nucleo:

- los dashboards llaman al backend
- los agentes llaman al backend
- el backend escribe en PostgreSQL y en Markdown
- PostgreSQL y Markdown no dependen de agentes ni interfaces

OpenClaw no debe escribir directamente en la base de datos. Si necesita crear, modificar o consultar informacion estructurada, debe hacerlo mediante Spock Core.

Los agentes si pueden operar sobre macOS y herramientas externas cuando la accion no modifica el estado gestionado de Spock.

Ejemplos:

```text
crear carpeta fuera de knowledge -> herramienta de sistema operativo
mover archivo en Descargas -> herramienta de sistema operativo
crear tarea -> Spock Core
relacionar nota con proyecto -> Spock Core
registrar archivo en Spock -> Spock Core
editar knowledge gestionado -> Spock Core o herramienta oficial de Spock
```

## Componentes

### Spock Core

Spock Core contiene la API principal y la logica de negocio.

Responsabilidades:

- validar operaciones
- coordinar cambios entre base de datos y archivos Markdown
- exponer endpoints para agentes e interfaces
- aplicar reglas de negocio
- mantener el modelo de dominio

### Database

Contiene lo relacionado con PostgreSQL:

- esquemas
- migraciones
- seeds
- documentacion del modelo de datos

La base de datos es la fuente de verdad para datos estructurados.

### Knowledge

Contiene la base de conocimiento en Markdown.

No debe mezclarse con documentacion interna del proyecto.

### Dashboards

Contiene interfaces de visualizacion y edicion.

No deben contener logica de negocio. Su responsabilidad es presentar informacion, capturar acciones del usuario y llamar a la API.

### Agents

Contiene agentes del sistema, incluido OpenClaw si se integra dentro del repositorio.

Los agentes son clientes de Spock Core para datos gestionados, y pueden usar herramientas del sistema operativo para acciones externas a Spock.

### Scripts

Contiene automatizaciones y utilidades de soporte.

Los scripts deben ser pequenos y explicitos. Si un script empieza a contener reglas de negocio importantes, esa logica debe moverse al backend.

### Docker

Contiene configuracion de contenedores y servicios locales.

### Backups

Contiene configuracion o resultados de copias de seguridad.

Debe evitarse guardar backups pesados en Git salvo que se decida explicitamente.

### Templates

Contiene plantillas Markdown reutilizables.

## Decision inicial

La arquitectura inicial sera modular pero no distribuida.

No se asume microservicios. En esta fase, un backend principal con PostgreSQL y filesystem local es suficiente y mas mantenible.

## Riesgos a vigilar

- que OpenClaw acumule demasiada logica de negocio
- que OpenClaw modifique datos gestionados por Spock saltandose Spock Core
- que los dashboards empiecen a escribir directamente en la base de datos
- que PostgreSQL termine almacenando documentos largos
- que Markdown y base de datos se desincronicen
- que se introduzca complejidad prematura antes de tener casos de uso claros
