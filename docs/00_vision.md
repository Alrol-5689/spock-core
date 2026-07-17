# Vision de Spock

## Que es Spock

Spock es un sistema operativo personal basado en IA.

Su objetivo es centralizar conocimiento, organizacion, productividad, automatizaciones y asistentes IA sobre datos propios del usuario.

Spock no es OpenClaw. OpenClaw sera uno de los agentes o componentes del sistema, pero no debe convertirse en el propietario de la arquitectura, la logica de negocio ni los datos.

## Nomenclatura

Spock es el nombre de la experiencia completa y del asistente personal al que el usuario habla.

Componentes recomendados:

```text
Spock
  experiencia de usuario, identidad del asistente y sistema completo

Spock Core
  backend/API con logica de negocio y operaciones sobre datos gestionados

spock
  base de datos PostgreSQL local

spockctl
  posible CLI futura para operaciones locales controladas
```

Ejemplo:

```text
Telegram -> Spock Agent -> Spock Core -> PostgreSQL / knowledge
```

OpenClaw puede ser el primer motor/agente que permita hablar con Spock desde Telegram u otras interfaces, pero debe seguir siendo reemplazable.

## Objetivo principal

Construir una plataforma personal que combine lo mejor de:

- Notion
- Obsidian
- OpenClaw
- una base de datos relacional
- una API propia
- dashboards personalizados
- agentes de IA

Todo ello debe funcionar sobre datos propios, formatos abiertos y componentes reemplazables.

## Principio central

Los datos son el activo mas importante.

Los modelos de IA, agentes, interfaces y servicios externos son reemplazables. El conocimiento debe seguir existiendo dentro de 20 anos aunque desaparezcan herramientas concretas como OpenClaw, Notion, Obsidian o cualquier proveedor de IA.

## Alcance inicial

La primera fase de Spock es de diseno arquitectonico.

No se prioriza programar rapido. Se prioriza definir una arquitectura simple, mantenible, escalable e independiente de proveedores concretos.

## Infraestructura inicial

En la primera fase, Spock se desarrollara y ejecutara en un Mac mini M1.

OpenClaw y los componentes locales funcionaran solamente cuando el ordenador este encendido.

Mas adelante, cuando exista un MacBook Pro como maquina principal, el Mac mini podra pasar a ser servidor 24/7.

No se plantea sustituir macOS por Linux salvo que aparezca una razon tecnica clara.

## Criterios de exito

Spock sera exitoso si:

- el conocimiento personal vive en Markdown legible sin depender de una aplicacion concreta
- los datos estructurados viven en una base de datos propia
- la logica de negocio esta centralizada en Spock Core
- los agentes interactuan con el sistema mediante interfaces controladas
- los dashboards no contienen logica de negocio
- los backups permiten reconstruir el sistema y sus datos
- cambiar modelos, agentes o interfaces no implica migrar el conocimiento principal
