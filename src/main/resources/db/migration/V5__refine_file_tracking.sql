alter table files add column display_name varchar(255);
alter table files add column storage_mode varchar(50) not null default 'REFERENCED';
alter table files add column last_seen_at timestamp with time zone;
alter table files add column missing_at timestamp with time zone;

create index idx_files_checksum_sha256 on files (checksum_sha256);
create index idx_files_storage_mode on files (storage_mode);
create index idx_files_missing_at on files (missing_at);

create table indexed_directories (
    id uuid primary key,
    path text not null unique,
    enabled boolean not null,
    recursive boolean not null,
    last_scanned_at timestamp with time zone,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

create index idx_indexed_directories_enabled on indexed_directories (enabled);
