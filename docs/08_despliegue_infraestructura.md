# Despliegue e infraestructura

## Estado inicial

Spock se ejecutara inicialmente en un Mac mini M1.

Durante esta fase, el sistema no necesita estar disponible 24/7. Los componentes locales funcionaran cuando el Mac mini este encendido.

## Estado temporal 2026-07-02

OpenClaw se ha instalado temporalmente en un portatil Ubuntu Server accesible por SSH en la red local.

Esta maquina se usa como runtime operativo 24/7 para el agente y el canal Telegram, mientras el Mac sigue siendo la maquina principal de trabajo humano y Obsidian.

Detalles operativos:

- Ubuntu host: `homeserver`
- usuario: `alex`
- gateway OpenClaw: `127.0.0.1:18789`
- servicio: `openclaw-gateway.service` como systemd user
- documentacion especifica: [15_openclaw_ubuntu_server.md](15_openclaw_ubuntu_server.md)

El vault de Obsidian no debe asumirse como una unica carpeta compartida entre Mac y Ubuntu. La estrategia prevista es una carpeta principal en Mac sincronizada con una carpeta equivalente en Ubuntu mediante Syncthing.

## Futuro previsto

Mas adelante:

- el MacBook Pro sera la maquina principal
- el Mac mini pasara a ser servidor 24/7
- Spock podra ejecutarse de forma mas permanente en la red local

Cuando el Mac mini quede delegado como servidor o maquina operativa de Spock, conviene contemplar una via sencilla de acceso remoto para intervencion humana.

Una opcion practica es instalar y configurar Escritorio Remoto de Chrome en el Mac mini. Esto permitiria conectarse desde el MacBook Pro cuando una interaccion con OpenClaw, Obsidian, navegador, Finder u otra aplicacion visual requiera supervision o accion manual.

Uso previsto:

- revisar el estado visual de OpenClaw u otros agentes
- resolver permisos o dialogos de macOS
- operar aplicaciones que no tengan API o CLI suficiente
- intervenir en automatizaciones bloqueadas
- administrar el Mac mini sin depender de monitor, teclado y raton conectados

Reglas:

- el acceso remoto es una herramienta de operacion, no una interfaz principal de Spock
- Spock debe seguir funcionando mediante API, CLI, servicios y automatizaciones reproducibles siempre que sea posible
- las acciones sensibles ejecutadas por control remoto deben quedar documentadas si afectan a arquitectura, datos o seguridad
- antes de exponer o habilitar acceso remoto permanente hay que revisar autenticacion, permisos de macOS y riesgos de seguridad

## Sistema operativo

macOS sigue siendo la opcion por defecto.

No se migrara a Linux salvo que exista una razon tecnica importante, como incompatibilidad critica, rendimiento insuficiente, limitaciones de automatizacion o necesidad clara de servicios de servidor.

## Docker

PostgreSQL se ejecutara inicialmente mediante Docker.

Docker tambien puede usarse para servicios auxiliares si simplifica la instalacion y el aislamiento.

La configuracion local inicial vive en `docker-compose.yml` y define un servicio `postgres` basado en la imagen oficial `postgres:17-alpine`.

El `docker-compose.yml` operativo debe estar dentro del repo clonable `spock-core`, no solo en una carpeta local agregadora del Mac. De ese modo, al ejecutar:

```bash
git clone https://github.com/Alrol-5689/spock-core.git
```

el servidor obtiene tambien la infraestructura minima para arrancar PostgreSQL y el backend.

Las credenciales locales se configuran mediante `.env`, que no debe subirse al repositorio. El archivo `.env.example` documenta las variables necesarias.

## Servicios iniciales

Servicios probables:

- PostgreSQL
- backend de Spock
- dashboards
- agentes
- scripts de backup

## Red

Inicialmente el acceso puede ser local.

Antes de exponer Spock a internet hay que definir:

- autenticacion
- autorizacion
- HTTPS
- backups
- logs
- actualizaciones
- superficie de ataque

## Backups

Spock debe poder restaurarse desde:

- backup de PostgreSQL
- copia de `knowledge`
- copia de `templates`
- configuracion relevante
- migraciones de base de datos

## Cuestiones abiertas

- estrategia de backup incremental
- cifrado de backups
- ubicacion de backups externos
- gestion de secretos
- arranque automatico en macOS
- monitorizacion basica
