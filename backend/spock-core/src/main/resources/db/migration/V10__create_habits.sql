create table habits (
    id uuid primary key references entities(id) on delete cascade,
    project_id uuid references projects(id),
    name varchar(255) not null,
    description text,
    active boolean not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    archived_at timestamp with time zone
);

create table habit_versions (
    id uuid primary key,
    habit_id uuid not null references habits(id) on delete cascade,
    starts_on date not null,
    ends_on date,
    frequency_type varchar(50) not null,
    target_count integer,
    weekdays text,
    active boolean not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint ck_habit_versions_date_range check (ends_on is null or ends_on >= starts_on)
);

create table habit_occurrences (
    id uuid primary key,
    habit_id uuid not null references habits(id) on delete cascade,
    habit_version_id uuid not null references habit_versions(id),
    due_date date not null,
    status varchar(50) not null,
    disabled boolean not null,
    skipped_reason text,
    notes text,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint uk_habit_occurrences_habit_version_date unique (habit_id, habit_version_id, due_date)
);

create index idx_habits_project_id on habits (project_id);
create index idx_habits_active on habits (active);
create index idx_habit_versions_habit_starts on habit_versions (habit_id, starts_on);
create index idx_habit_versions_active on habit_versions (active);
create index idx_habit_occurrences_habit_due_date on habit_occurrences (habit_id, due_date);
create index idx_habit_occurrences_status on habit_occurrences (status);
