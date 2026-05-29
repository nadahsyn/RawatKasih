create table if not exists public.users (
  id bigserial primary key,
  auth_id uuid unique,
  name text not null,
  email text unique,
  phone text,
  role text not null check (role in ('caregiver', 'patient')),
  caregiver_id bigint references public.users(id) on delete set null,
  age integer,
  gender text,
  created_at timestamptz not null default now()
);

create table if not exists public.medicine_schedules (
  id bigserial primary key,
  patient_id bigint not null references public.users(id) on delete cascade,
  medicine_name text not null,
  dosage text,
  schedule_time text not null,
  note text,
  created_at timestamptz not null default now()
);

create table if not exists public.medicine_logs (
  id bigserial primary key,
  schedule_id bigint not null references public.medicine_schedules(id) on delete cascade,
  patient_id bigint not null references public.users(id) on delete cascade,
  taken_date date not null,
  taken_at timestamptz,
  status text not null default 'taken',
  created_at timestamptz not null default now()
);

create table if not exists public.daily_conditions (
  id bigserial primary key,
  patient_id bigint not null references public.users(id) on delete cascade,
  date date not null,
  condition text not null,
  mood text not null,
  blood_pressure text,
  created_at timestamptz not null default now()
);

create index if not exists users_auth_id_idx on public.users(auth_id);
create index if not exists users_caregiver_id_idx on public.users(caregiver_id);
create index if not exists medicine_schedules_patient_id_idx on public.medicine_schedules(patient_id);
create index if not exists medicine_logs_patient_date_idx on public.medicine_logs(patient_id, taken_date);
create index if not exists daily_conditions_patient_date_idx on public.daily_conditions(patient_id, date);

-- MVP seed example for a manually created caregiver auth account:
-- insert into public.users (auth_id, name, email, role)
-- values ('00000000-0000-0000-0000-000000000000', 'Suster Dina', 'caregiver@example.com', 'caregiver');
