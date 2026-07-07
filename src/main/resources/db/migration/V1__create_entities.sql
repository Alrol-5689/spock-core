create table entities (
    id uuid primary key,
    entity_type varchar(50) not null,
    title varchar(255) not null,
    slug varchar(255),
    summary text,
    status varchar(50),
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    archived_at timestamp with time zone
);

create index idx_entities_entity_type on entities (entity_type);
create index idx_entities_status on entities (status);
create index idx_entities_slug on entities (slug);
