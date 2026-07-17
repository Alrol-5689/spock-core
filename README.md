# Spock

Repositorio principal de Spock.

## Estructura

- `backend/spock-core`: API principal en Kotlin, Spring Boot, Gradle y PostgreSQL.
- `database`: documentacion, SQL auxiliar, seeds o dumps. La base de datos viva queda en un volumen Docker.
- `docs`: documentacion funcional y tecnica.
- `scripts`, `agents`, `dashboards`, `templates`: soporte operativo del proyecto.
- `backups`: copias locales no versionadas, salvo `.gitkeep`.

## Arranque local

```sh
cp .env.example .env
docker compose up -d postgres
cd backend/spock-core
SPRING_DOCKER_COMPOSE_ENABLED=false ./gradlew bootRun
```

La API arranca por defecto en `http://localhost:8080`.

```sh
curl http://localhost:8080/actuator/health
```

## Backend

La documentacion especifica de la API esta en `backend/spock-core/README.md`.
La infraestructura compartida se gestiona desde el `docker-compose.yml` de la raiz.
