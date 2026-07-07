alter table projects add column due_date date;

create index idx_projects_due_date on projects (due_date);
