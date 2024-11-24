
-- email validation
CREATE OR REPLACE FUNCTION is_valid_email(email TEXT) 
RETURNS BOOLEAN AS $$
BEGIN
    RETURN email ~ '^[A-Za-z0-9_%+-]+@[A-Za-z0-9-]+\.[A-Za-z]{2,}$';
END;
$$ LANGUAGE plpgsql;

-- is_blank
Create OR REPLACE FUNCTION is_blank(s TEXT)
RETURNS BOOLEAN AS $$
BEGIN
	RETURN LENGTH(TRIM(s))<=0;
END;
$$ LANGUAGE plpgsql;

--single name validation
Create OR REPLACE FUNCTION is_valid_name(s TEXT)
RETURNS BOOLEAN AS $$
BEGIN
	RETURN  s ~ '^[a-zA-Z]+$';
END;
$$ LANGUAGE plpgsql;

--fullname validation
Create OR REPLACE FUNCTION is_valid_fullname(s TEXT)
RETURNS BOOLEAN AS $$
BEGIN 
	RETURN s ~ '^[A-Za-z]+( [A-Za-z]+)*$';
END;
$$ LANGUAGE plpgsql;

-- username validation
Create OR REPLACE FUNCTION is_valid_username(s TEXT)
RETURNS BOOLEAN AS $$
BEGIN
	RETURN s ~ '^[A-Za-z]+[A-Za-z_0-9]{3,}$';
END;
$$ LANGUAGE plpgsql;


create table app_role(
role_id serial primary key,
name varchar(20) unique not null check(is_valid_name(name))
);

create table app_user(
user_id serial primary key,
username varchar(50) unique not null check( is_valid_username(username)),
password varchar(100) not null check(not is_blank(password))
);

create table users_roles(
user_id bigint not null, 
role_id bigint not null,
Constraint user_fk Foreign key(user_id) 
	references app_user(user_id),
Constraint role_fk FOREIGN KEY(role_id) 
	references app_role(role_id),
Constraint uq_users_roles Unique(user_id,role_id)
);

create table department (
dept_id serial primary key,
dept_name varchar(100) unique not null check(is_valid_fullname(dept_name))
);

create table employee(
emp_id serial primary key,
first_name varchar(30) not null check(is_valid_name(first_name)),
last_name varchar(30) not null check(is_valid_name(last_name)),
email varchar(100) unique not null check(is_valid_email(email)),
position varchar(100) not null check(is_valid_fullname(position)),
salary double precision not null check(salary>0 and salary<100000),
dept_id bigint not null,
Constraint dept_fk FOREIGN KEY(dept_id)
	references department(dept_id)
);

create table token(
user_id bigint not null,
token text not null ,
created_at timestamp not null
);

