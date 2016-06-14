create table [Person] (
	id varchar not null primary key,
	name varchar not null,
	gender varchar not null,
	birth_date integer not null
);

create table [Collaborator] (
	person_id varchar not null primary key,
	start_date integer not null,
	release_date integer,
	observation varchar,
	usercredentials_id varchar not null
);

create table [Telephone] (
	id varchar not null primary key,
	person_id varchar not null,
	area_code int not null,
	number int not null,
	phone_type int not null
);

create table [UserCredentials] (
	id varchar not null primary key,
	username varchar not null,
	password varchar not null,
	user_role varchar not null
);

/* 

For the future:

> create "Administrator Role"
	. create / modify collaborator
	. create / modify student
	. create / modify teacher 
	. create / modify class
	. start class run
	. end class run
	. create / modify event

> create "Collaborator Role"
	. view events

- create "Teacher Role"
	. create / modify student
	. create / modify class
	. start class run
	. end class run

- create "Financial Role"
	. add credit financial entry
	. add debit financial entry
	. view financial report (*** which financial reports are?)

- create "Student Role"
	. view class posts

 */

create table [Student] (
	person_id varchar not null primary key,
	observation varchar,
	usercredentials_id varchar not null
);

create table [Teacher] (
	person_id varchar not null primary key,
	usercredentials_id varchar not null
);

create table [Class] (
	id varchar not null primary key,
	name varchar not null,
	description varchar not null,
	teacher_id varchar not null
);

create table [ClassRun] (
	class_id varchar not null,
	start_date integer not null,
	end_date integer not null,
	class_run_type int not null -- Daily, Weekly, Monthly
);

create table [Post] (
	id varchar not null primary key,
	content varchar not null,
	visibility_type int not null, -- Public or Internal
	post_type varchar not null, -- Class or General
	created_by varchar not null,
	created integer not null,
	published_by varchar not null,
	published_date integer not null,
	modified integer not null,
	modified_by varchar not null
);

create table [Event] (
	id varchar not null primary key,
	name varchar not null,
	description varchar not null,
	start_date integer not null,
	end_date integer not null,
	observation varchar,
	visibility_type int not null, -- Public or Internal
	created_by varchar not null,
	created integer not null,
	modified_by varchar not null,
	modified integer not null
);

CREATE TABLE [FinancialReference] (
  [id] varchar not null primary key,
  [description] varchar not null,
  [category] int not null,
  [associated_with_collaborator] bit not null
);

create table [FinancialEntry] (
	[id] varchar not null primary key,
	[entry_date] integer not null,
	[entry_value] real not null,
	[balance] real not null,
	[reference_entry] varchar not null,
	[additional_text] varchar,
	[target_id] varchar not null
);

create table [FinancialEntryTarget] (
	[id] varchar not null,
	[name] varchar,
	[type] int not null
);

create table [FinancialBalance] (
    [balance] real not null
);