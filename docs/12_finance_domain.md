# Dominio Finance

## Objetivo

El dominio `finance` gestiona economia personal dentro de Spock Core.

Parte de la idea del proyecto antiguo `personal-economy`, pero adaptada a la arquitectura actual:

- Kotlin y Spring Boot
- PostgreSQL como fuente de verdad
- migraciones con Flyway
- una sola base de datos de Spock
- sin modelo de usuarios por ahora, porque Spock empieza como single-user
- cantidades monetarias con `BigDecimal`, no `Double`

## Alcance inicial

El dominio debe poder registrar:

- cuentas financieras
- ingresos
- gastos
- reembolsos asociados a gastos
- snapshots mensuales de capital liquido
- calculos de cashflow y runway en una capa de servicio futura

## Organizacion interna

`finance` es un dominio de negocio de Spock Core.

Dentro del dominio, el codigo se organiza por subdominio:

```text
com.alejandro.spock.core.finance.account
  model
  repository
  service
  controller
  dto

com.alejandro.spock.core.finance.transaction
  model
  repository
  service
  controller
  dto

com.alejandro.spock.core.finance.reimbursement
  model
  repository
  service
  controller
  dto

com.alejandro.spock.core.finance.capital
  model
  repository
  service
  controller
  dto

com.alejandro.spock.core.finance.report
  service
  dto
```

Notas:

- `report` no necesita `model` al inicio porque representa calculos derivados, no tablas propias
- no debe existir una carpeta global `finance.dto` con todos los DTO mezclados
- cada subdominio debe mantener cerca sus modelos, servicios, repositorios y endpoints

## Tablas iniciales

### `finance_accounts`

Representa una cuenta o contenedor de dinero.

Ejemplos:

- cuenta corriente
- cuenta de ahorro
- efectivo
- hucha
- broker
- crypto

Campos principales:

```text
id uuid primary key
name text not null
account_type text not null
institution text
currency varchar(3) not null
initial_balance numeric(19, 2) not null
is_active boolean not null
created_at timestamptz not null
updated_at timestamptz not null
```

### `finance_transactions`

Representa ingresos y gastos.

Campos principales:

```text
id uuid primary key
account_id uuid references finance_accounts(id)
name text not null
description text
amount numeric(19, 2) not null
direction text not null
income_category text
expense_category text
transaction_date date not null
created_at timestamptz not null
updated_at timestamptz not null
```

Regla:

- si `direction = INCOME`, se usa `income_category`
- si `direction = EXPENSE`, se usa `expense_category`
- la API debera validar esta coherencia

### `finance_reimbursements`

Representa dinero recuperado sobre un gasto.

Ejemplo:

```text
Gasto: cena grupo 80 EUR
Reembolso Laura: 20 EUR
Reembolso Carlos: 20 EUR
Gasto neto: 40 EUR
```

Campos principales:

```text
id uuid primary key
transaction_id uuid not null references finance_transactions(id)
amount numeric(19, 2) not null
payer_name text
note text
reimbursement_date date not null
```

### `finance_monthly_capital_snapshots`

Guarda el capital liquido disponible por mes.

Campos principales:

```text
id uuid primary key
version bigint not null
month varchar(7) not null unique
savings_account numeric(19, 2) not null
piggy_bank numeric(19, 2) not null
checking_account numeric(19, 2) not null
cash numeric(19, 2) not null
```

Uso:

- calcular evolucion de capital
- calcular runway
- comparar ahorro mensual

## Categorias iniciales

Ingresos:

```text
SALARY
BONUS
FREELANCE
OTHER_ACTIVE
REAL_ESTATE_SALE
STOCK_SALE
BOND_SALE
MUTUAL_FUND_SALE
CRYPTO_SALE
BUSINESS_SALE
OTHER_CAPITAL_GAIN
INTEREST
DIVIDENDS
RENTAL
COMMISSIONS
OTHER_PASSIVE
OTHER
```

Gastos:

```text
FIXED
ESSENTIAL
DISCRETIONARY
```

## Decisiones

- No se copia el proyecto antiguo literalmente.
- No se introduce multiusuario todavia.
- No se usa `Double` para dinero.
- No se separa `finance` en otra API por ahora.
- Spock Core mantiene una arquitectura de monolito modular.

## Pendiente

- repositorios
- servicios de cashflow
- endpoints REST
- validaciones de coherencia entre `direction` y categorias
- relacion futura entre transacciones financieras y entidades GTD si hiciera falta
