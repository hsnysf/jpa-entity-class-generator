drop schema public cascade;

create schema public;

create table public.address
(
	addr_id serial,
	addr_building integer,
	addr_road integer,
	addr_block integer,
	addr_city character varying(100),
	constraint pk_addr_id primary key (addr_id)
);

create table public.employee
(
	emp_id serial,
	emp_name character varying(100),
	emp_gender char(1),
	emp_age smallint,
	emp_cpr int,
	emp_account_no bigint,
	emp_gpa real,
	emp_salary double precision,
	emp_annual_income decimal,
	emp_date_of_birth date,
	emp_registration_time timestamp,
	emp_sleep_time time,
	emp_graduated boolean,
	emp_hobbies character varying(100)[],
	emp_address_id integer,
	constraint pk_emp_id primary key (emp_id),
	constraint fk_address_id foreign key (emp_address_id)
	references public.address (addr_id)
);

create table public.certificate
(
	cert_id serial,
	cert_employee_id integer,
	cert_name character varying(200),
	cert_year integer,
	constraint pk_cert_id primary key (cert_id),
	constraint fk_employee_id foreign key (cert_employee_id)
	references public.employee (emp_id)
);

create table public.manager
(
	mngr_id integer,
	mngr_degree integer,
	mngr_allowance decimal,
	constraint pk_mngr_id primary key (mngr_id),
	constraint fk_mngr_id foreign key (mngr_id)
	references public.employee (emp_id)
);