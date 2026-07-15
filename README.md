# Spock Core

Backend principal de Spock, construido con Kotlin, Spring Boot, Gradle y PostgreSQL.

## Requisitos

- Java 21
- Docker
- Docker Compose plugin

En Ubuntu:

```sh
sudo apt update
sudo apt install -y openjdk-21-jdk docker.io docker-compose-plugin
sudo usermod -aG docker "$USER"
```

Cierra la sesion SSH y vuelve a entrar despues de cambiar el grupo Docker.

## Configuracion local

```sh
cp .env.example .env
```

Edita `.env` si quieres cambiar usuario, password, base de datos o puerto local de PostgreSQL.

## Arranque

Desde la raiz del repo:

```sh
docker compose up -d postgres
./gradlew bootRun
```

La API arranca por defecto en `http://localhost:8080`.

Comprobacion:

```sh
curl http://localhost:8080/actuator/health
```

## Servicio permanente en Ubuntu

En el servidor de Alex, la API se ejecuta como servicio de usuario systemd:

```sh
systemctl --user status spock-core.service
```

El servicio usa:

- Unidad instalada: `~/.config/systemd/user/spock-core.service`
- Unidad versionada: `deploy/systemd/spock-core.service`
- Directorio de trabajo: `/home/alex/repos/spock-core`
- Configuracion local: `.env`
- Jar: `build/libs/spock-core-0.0.1-SNAPSHOT.jar`
- PostgreSQL: `docker compose up -d postgres`

Instalacion de la unidad versionada:

```sh
mkdir -p ~/.config/systemd/user
cp deploy/systemd/spock-core.service ~/.config/systemd/user/spock-core.service
systemctl --user daemon-reload
systemctl --user enable --now spock-core.service
```

Comandos utiles:

```sh
# Ver estado
systemctl --user status spock-core.service

# Ver logs
journalctl --user -u spock-core.service -f

# Reiniciar despues de cambios
systemctl --user restart spock-core.service

# Reconstruir jar y reiniciar
./gradlew bootJar
systemctl --user restart spock-core.service
```

La API deberia quedar disponible en:

```sh
curl http://localhost:8080/actuator/health
```

Si cambia la configuracion de la unidad:

```sh
systemctl --user daemon-reload
systemctl --user restart spock-core.service
```

## Variables principales

- `SPOCK_CORE_PORT`: puerto HTTP de la aplicacion. Por defecto `8080`.
- `SPOCK_DATABASE_URL`: JDBC URL de PostgreSQL. Por defecto `jdbc:postgresql://localhost:5432/spock`.
- `SPOCK_DATABASE_USERNAME`: usuario de PostgreSQL. Por defecto `spock`.
- `SPOCK_DATABASE_PASSWORD`: password de PostgreSQL. Por defecto `spock_dev_password`.

Si PostgreSQL se gestiona fuera de este repo, puedes desactivar la integracion Docker Compose de Spring:

```sh
SPRING_DOCKER_COMPOSE_ENABLED=false ./gradlew bootRun
```
