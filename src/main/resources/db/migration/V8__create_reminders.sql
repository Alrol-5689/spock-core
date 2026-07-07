create table reminders (
    id uuid primary key,
    entity_id uuid not null references entities(id) on delete cascade,
    remind_at timestamp with time zone not null,
    title varchar(255) not null,
    message text,
    status varchar(50) not null,
    delivery_channel varchar(50) not null,
    external_provider varchar(50),
    external_id text,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    sent_at timestamp with time zone,
    cancelled_at timestamp with time zone,
    last_error text
);

create index idx_reminders_entity_id on reminders (entity_id);
create index idx_reminders_status_remind_at on reminders (status, remind_at);
create index idx_reminders_delivery_channel on reminders (delivery_channel);
create index idx_reminders_external_provider_id on reminders (external_provider, external_id);
