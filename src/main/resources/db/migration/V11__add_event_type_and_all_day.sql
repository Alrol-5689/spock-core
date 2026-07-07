alter table events add column event_type varchar(50) not null default 'OTHER';
alter table events add column all_day boolean not null default false;

create index idx_events_event_type on events (event_type);
create index idx_events_all_day on events (all_day);
