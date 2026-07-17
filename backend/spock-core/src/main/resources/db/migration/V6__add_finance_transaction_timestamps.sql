alter table finance_transactions add column created_at timestamp with time zone not null default now();
alter table finance_transactions add column updated_at timestamp with time zone not null default now();
