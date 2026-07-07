create table daily_logs (
    id uuid primary key references entities(id) on delete cascade,
    log_date date not null unique
);

create index idx_daily_logs_log_date on daily_logs (log_date);
