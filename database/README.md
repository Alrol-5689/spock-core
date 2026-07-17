# Database

Spock uses PostgreSQL for structured data.

## Local development

Create a local environment file:

```sh
cp .env.example .env
```

Start PostgreSQL:

```sh
docker compose up -d postgres
```

Check status:

```sh
docker compose ps
```

Connect with `psql` from inside the container:

```sh
docker compose exec postgres psql -U spock -d spock
```

Stop PostgreSQL:

```sh
docker compose down
```

Delete local database data:

```sh
docker compose down -v
```

Only use `docker compose down -v` when you intentionally want to delete the local PostgreSQL volume.
