alter table areas add column type varchar(50) not null default 'PERSONAL';

create index idx_areas_type on areas (type);
