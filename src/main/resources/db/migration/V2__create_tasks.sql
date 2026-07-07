create table tasks (
    id uuid primary key references entities(id) on delete cascade,
    status varchar(50) not null,
    priority varchar(50),
    due_at timestamp with time zone,
    scheduled_at timestamp with time zone,
    completed_at timestamp with time zone
);

create index idx_tasks_status on tasks (status);
create index idx_tasks_priority on tasks (priority);
create index idx_tasks_due_at on tasks (due_at);
create index idx_tasks_scheduled_at on tasks (scheduled_at);
