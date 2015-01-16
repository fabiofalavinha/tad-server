create table [Person] (
	id varchar not null primary key,
	name varchar not null,
	email varchar not null,
	sex int not null,
	birth_date datetime not null
)

create table [Collaborator] (
	person_id varchar not null primary key,
	start_date datetime not null,
	release_date datetime,
	observation varchar,
	usercredentials_id varchar not null
)

create table [Telephone] (
	id varchar not null primary key,
	collaborator_id varchar not null,
	code_area int not null,
	phone_number int not null,
	phone_type int not null
)

create table [UserCredentials] (
	id varchar not null primary key,
	username varchar not null,
	password varchar not null
)

/* 

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

create table [UserPermission] (
	name varchar not null,
	user_role_id varchar not null	
)

create table [UserRole] (
	name varchar not null
)

create table [Student] (
	person_id varchar not null primary key,
	observation varchar,
	usercredentials_id varchar not null
)

create table [Teacher] (
	person_id varchar not null primary key,
	usercredentials_id varchar not null
)

create table [Class] (
	id varchar not null primary key,
	name varchar not null,
	description varchar not null,
	teacher_id varchar not null
)

create table [ClassRun] (
	class_id varchar not null,
	start_date datetime not null,
	end_date datetime not null,
	class_run_type int not null -- Daily, Weekly, Monthly
)

create table [Post] (
	id varchar not null primary key,
	content varchar not null,
	visibility_type int not null, -- Public or Internal
	post_type varchar not null, -- Class or General
	created_by varchar not null,
	created datetime not null,
	published_by varchar not null,
	published_date datetime not null,
	modified datetime not null,
	modified_by varchar not null
)

create table [Event] (
	id varchar not null primary key,
	name varchar not null,
	description varchar not null,
	start_date datetime not null,
	end_date datetime not null,
	observation varchar,
	visibility_type int not null, -- Public or Internal
	created_by varchar not null,
	created datetime not null,
	modified_by varchar not null,
	modified datetime not null
)
