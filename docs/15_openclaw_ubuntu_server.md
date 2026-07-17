# OpenClaw en Ubuntu server

## Estado

Temporal operativo.

OpenClaw esta instalado y ejecutandose en un portatil Ubuntu Server de la red local.

Servidor:

- host: `homeserver`
- usuario: `alex`
- acceso SSH: `alex@192.168.1.144`
- sistema: Ubuntu 24.04 LTS
- runtime OpenClaw: npm global en `~/.npm-global`
- gateway: systemd user service `openclaw-gateway.service`
- gateway local: `127.0.0.1:18789`

La configuracion vive en:

```text
/home/alex/.openclaw/openclaw.json
```

Los logs se consultan preferentemente con:

```bash
journalctl --user -u openclaw-gateway.service -n 120 --no-pager
journalctl --user -u openclaw-gateway.service -f
```

## Razon

El objetivo inmediato es disponer de un asistente operativo 24/7 sin depender de que el Mac este encendido.

Esto no cambia la arquitectura de producto:

- Spock sigue siendo el sistema.
- Spock Core sigue siendo la API/backend propietario de la logica de negocio.
- OpenClaw sigue siendo un agente operativo reemplazable.

## Canales

El canal inicial de comunicacion es Telegram.

Bot:

```text
@alex_spock_bot
```

Reglas:

- el bot debe estar restringido por allowlist
- solo el Telegram user ID del propietario debe poder usar DMs
- `commands.ownerAllowFrom` debe contener el propietario
- los tokens de Telegram no deben aparecer en capturas, commits, notas ni logs compartidos
- si un token se expone, debe revocarse en BotFather con `/revoke` y actualizarse en OpenClaw

Configuracion esperada:

```bash
openclaw config get channels.telegram.dmPolicy
openclaw config get channels.telegram.allowFrom
openclaw config get commands.ownerAllowFrom
```

Valores esperados:

```text
allowlist
["<TELEGRAM_USER_ID>"]
["telegram:<TELEGRAM_USER_ID>"]
```

El token se guarda en:

```text
channels.telegram.botToken
```

OpenClaw lo muestra como `__OPENCLAW_REDACTED__`, lo cual es correcto.

## Busqueda web

El proveedor inicial de busqueda web es Codex Hosted Search.

Configuracion:

```bash
openclaw config set tools.web.search.enabled true
openclaw config set tools.web.search.provider codex
openclaw gateway restart
```

Brave Search queda como alternativa si se decide usar una Brave Search API key.

Instalar el navegador Brave en el Mac no configura la API de Brave Search.

## Carpetas de trabajo

OpenClaw corre en Ubuntu y no puede acceder directamente a rutas de macOS como:

```text
/Users/alejandrocordobarodriguez/Documents/spock-knowledge
/Users/alejandrocordobarodriguez/repos/Spock
```

Las rutas equivalentes previstas en Ubuntu son:

```text
/home/alex/vaults/spock-knowledge
/home/alex/repos/spock-core
```

La carpeta de Obsidian debe vivir como vault principal en el Mac y sincronizarse con Ubuntu.

Mac:

```text
/Users/alejandrocordobarodriguez/Library/CloudStorage/Dropbox/spock-knowledge
```

Ubuntu:

```text
/home/alex/vaults/spock-knowledge
```

Herramienta recomendada para sincronizacion inicial:

```text
Syncthing
```

Razon:

- permite trabajar en Obsidian desde el Mac
- permite a Spock/OpenClaw leer y escribir en Ubuntu
- evita exponer la carpeta por red como dependencia principal
- mantiene el sistema usable aunque una maquina este apagada temporalmente

## API y base de datos

Cuando Spock Core se ejecute de forma permanente, debe levantarse en Ubuntu.

PostgreSQL tambien puede ejecutarse en Ubuntu, preferentemente mediante Docker.

El repo operativo que debe clonarse en Ubuntu es:

```text
https://github.com/Alrol-5689/spock-core.git
```

La raiz de ese repo debe contener lo necesario para arrancar el backend localmente:

```text
docker-compose.yml
.env.example
README.md
gradlew
src/
```

La documentacion larga de Spock vive como conocimiento en el vault, no como requisito para arrancar el backend:

```text
/home/alex/vaults/spock-knowledge/040_Resources/spock-docs
```

Las consultas pueden hacerse de dos formas:

- terminal por SSH para administracion rapida
- DBeaver en Mac mediante tunel SSH para exploracion visual

Ejemplo de tunel PostgreSQL:

```bash
ssh -L 5432:127.0.0.1:5432 alex@192.168.1.144
```

Ejemplo de tunel MySQL si existe un servicio en `3306` en Ubuntu:

```bash
ssh -L 3307:127.0.0.1:3306 alex@192.168.1.144
```

En DBeaver se conecta contra `localhost` y el puerto local del tunel.

No se deben exponer puertos de base de datos directamente en la LAN o internet salvo decision explicita.

## Operacion

Comandos utiles:

```bash
openclaw gateway status
openclaw gateway restart
systemctl --user is-enabled openclaw-gateway.service
journalctl --user -u openclaw-gateway.service -f
```

Si `openclaw` no esta en el PATH:

```bash
export PATH="$HOME/.npm-global/bin:$PATH"
```

Esto debe quedar persistido en `~/.bashrc`.

## Prompt inicial para Spock

Prompt operativo recomendado para dar contexto a Spock:

```text
Eres Spock, mi asistente personal. Yo soy Alex.

Quiero que actues como un asistente practico, directo y con buena memoria operativa. Tu trabajo es ayudarme a organizar mi conocimiento, mis proyectos, mis notas, mis tareas y mis decisiones tecnicas/personales. Prefiero respuestas claras, accionables y sin relleno.

Tu tono:
- Hablame en espanol salvo que te pida otra cosa.
- Se directo, pragmatico y ordenado.
- Si falta contexto, pregunta lo minimo necesario.
- Si ves un riesgo o una mala decision, dimelo claramente y propon una alternativa.
- No inventes datos. Distingue entre lo que sabes, lo que infieres y lo que habria que comprobar.

Mis espacios de trabajo previstos son:
- Knowledge vault / Obsidian: /home/alex/vaults/spock-knowledge
- Repositorio operativo de Spock Core: /home/alex/repos/spock-core

En mi Mac, las rutas equivalentes son:
- /Users/alejandrocordobarodriguez/Library/CloudStorage/Dropbox/spock-knowledge
- /Users/alejandrocordobarodriguez/repos/Spock/backend/spock-core

Ten en cuenta que estas corriendo en el Ubuntu server. Si necesitas leer o escribir archivos, usa las rutas del servidor, no las rutas de macOS, salvo que te diga explicitamente que una ruta esta montada o sincronizada.

Quiero usar el vault de Obsidian como memoria y base de conocimiento. Cuando trabajemos con notas:
- Manten los archivos en Markdown.
- Usa nombres claros y estables.
- No reorganices carpetas grandes sin pedirme confirmacion.
- Si creas una nota nueva, ponla en una ubicacion logica y explicame donde.
- Prefiero estructuras simples: inbox, projects, areas, resources, archive.
- Ayudame a convertir ideas sueltas en notas utiles, planes, checklists o documentacion.

Cuando trabajemos con codigo:
- Lee el proyecto antes de proponer cambios.
- Respeta el estilo existente.
- Prioriza soluciones simples y mantenibles.
- Antes de tocar archivos importantes, explicame que vas a cambiar.
- Despues de cambiar algo, dime como verificarlo.

Quiero que me ayudes especialmente con:
- Mantener mi sistema de notas y conocimiento.
- Gestionar proyectos personales y tecnicos.
- Preparar documentacion.
- Recordar decisiones y contexto importante.
- Automatizar tareas repetitivas.
- Revisar ideas y convertirlas en proximos pasos.
- Ayudarme a trabajar con el repositorio Spock.

Si te pido que recuerdes algo importante, sugiere donde guardarlo en el vault. Si detectas que una conversacion contiene una decision relevante, proponme convertirla en nota.

Estado actual que debes conocer:
- OpenClaw corre en Ubuntu Server como servicio systemd de usuario.
- El canal Telegram esta activo y restringido por allowlist.
- El proveedor de busqueda web configurado es Codex Hosted Search.
- El vault de Obsidian debe sincronizarse entre Dropbox en Mac y `/home/alex/vaults/spock-knowledge` en Ubuntu.
- La documentacion de Spock debe estar disponible en `040_Resources/spock-docs` dentro del vault.
- Spock Core/API debe levantarse en Ubuntu desde `/home/alex/repos/spock-core`.

De momento, espera mi siguiente instruccion. Si necesitas comprobar estado, pide permiso y di exactamente que comando quieres ejecutar.
```

## Pendientes

- instalar y configurar Syncthing en Mac y Ubuntu
- crear `/home/alex/vaults/spock-knowledge`
- clonar o actualizar `/home/alex/repos/spock-core`
- levantar Spock Core en Ubuntu
- decidir PostgreSQL local/Docker y estrategia de backups
- documentar como actualizar el token Telegram sin exponer secretos
