create table projects (
    id uuid primary key references entities(id) on delete cascade,
    status varchar(50) not null,
    started_at date,
    ended_at date
);

create table areas (
    id uuid primary key references entities(id) on delete cascade,
    status varchar(50) not null
);

create table events (
    id uuid primary key references entities(id) on delete cascade,
    starts_at timestamp with time zone not null,
    ends_at timestamp with time zone,
    location text
);

create table people (
    id uuid primary key references entities(id) on delete cascade,
    display_name varchar(255) not null,
    email varchar(255),
    phone varchar(100)
);

create table pages (
    id uuid primary key,
    entity_id uuid not null unique references entities(id) on delete cascade,
    title varchar(255) not null,
    markdown_path text not null unique,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    last_synced_at timestamp with time zone
);

create table files (
    id uuid primary key,
    file_path text not null unique,
    original_filename varchar(255),
    file_kind varchar(50) not null,
    mime_type varchar(255),
    size_bytes bigint,
    checksum_sha256 varchar(64),
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

create table page_files (
    id uuid primary key,
    page_id uuid not null references pages(id) on delete cascade,
    file_id uuid not null references files(id) on delete cascade,
    created_at timestamp with time zone not null,
    constraint uk_page_files_page_file unique (page_id, file_id)
);

create table entity_relations (
    id uuid primary key,
    source_entity_id uuid not null references entities(id) on delete cascade,
    target_entity_id uuid not null references entities(id) on delete cascade,
    relation_type varchar(50) not null,
    created_at timestamp with time zone not null
);

create table tags (
    id uuid primary key,
    name varchar(100) not null unique,
    created_at timestamp with time zone not null
);

create table entity_tags (
    id uuid primary key,
    entity_id uuid not null references entities(id) on delete cascade,
    tag_id uuid not null references tags(id) on delete cascade,
    created_at timestamp with time zone not null,
    constraint uk_entity_tags_entity_tag unique (entity_id, tag_id)
);

create table file_indexes (
    file_id uuid primary key references files(id) on delete cascade,
    indexed_at timestamp with time zone,
    last_synced_at timestamp with time zone,
    content_hash text,
    ocr_status varchar(50),
    ocr_text_path text,
    embedding_status varchar(50),
    embedding_id text,
    index_version integer,
    error_message text
);

create index idx_projects_status on projects (status);
create index idx_areas_status on areas (status);
create index idx_events_starts_at on events (starts_at);
create index idx_people_display_name on people (display_name);
create index idx_pages_entity_id on pages (entity_id);
create index idx_files_file_kind on files (file_kind);
create index idx_entity_relations_source on entity_relations (source_entity_id);
create index idx_entity_relations_target on entity_relations (target_entity_id);
create index idx_entity_relations_type on entity_relations (relation_type);
create index idx_entity_tags_entity_id on entity_tags (entity_id);
create index idx_entity_tags_tag_id on entity_tags (tag_id);
