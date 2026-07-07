alter table habits add column value_type varchar(50) not null default 'BOOLEAN';
alter table habits add column unit varchar(50);

alter table habit_occurrences add column numeric_value numeric(12, 4);
alter table habit_occurrences add column count_value integer;
alter table habit_occurrences add column duration_seconds integer;
alter table habit_occurrences add column text_value text;
alter table habit_occurrences add column recorded_at timestamp with time zone;

alter table habit_occurrences add constraint ck_habit_occurrences_count_value_non_negative check (count_value is null or count_value >= 0);
alter table habit_occurrences add constraint ck_habit_occurrences_duration_seconds_non_negative check (duration_seconds is null or duration_seconds >= 0);
alter table habit_occurrences add constraint ck_habit_occurrences_single_value check (
    (case when numeric_value is null then 0 else 1 end) +
    (case when count_value is null then 0 else 1 end) +
    (case when duration_seconds is null then 0 else 1 end) +
    (case when text_value is null then 0 else 1 end) <= 1
);

create index idx_habits_value_type on habits (value_type);
create index idx_habit_occurrences_recorded_at on habit_occurrences (recorded_at);
