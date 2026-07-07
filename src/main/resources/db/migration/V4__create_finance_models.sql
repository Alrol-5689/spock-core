create table finance_accounts (
    id uuid primary key,
    name varchar(255) not null,
    account_type varchar(50) not null,
    institution varchar(255),
    currency varchar(3) not null,
    initial_balance numeric(19, 2) not null,
    is_active boolean not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

create table finance_transactions (
    id uuid primary key,
    account_id uuid references finance_accounts(id),
    name varchar(255) not null,
    description text,
    amount numeric(19, 2) not null,
    direction varchar(50) not null,
    income_category varchar(50),
    expense_category varchar(50),
    transaction_date date not null
);

create table finance_reimbursements (
    id uuid primary key,
    transaction_id uuid not null references finance_transactions(id) on delete cascade,
    amount numeric(19, 2) not null,
    payer_name varchar(255),
    note text,
    reimbursement_date date not null
);

create table finance_monthly_capital_snapshots (
    id uuid primary key,
    version bigint not null,
    month varchar(7) not null unique,
    savings_account numeric(19, 2) not null,
    piggy_bank numeric(19, 2) not null,
    checking_account numeric(19, 2) not null,
    cash numeric(19, 2) not null
);

create index idx_finance_accounts_type on finance_accounts (account_type);
create index idx_finance_transactions_account_id on finance_transactions (account_id);
create index idx_finance_transactions_direction on finance_transactions (direction);
create index idx_finance_transactions_date on finance_transactions (transaction_date);
create index idx_finance_reimbursements_transaction_id on finance_reimbursements (transaction_id);
