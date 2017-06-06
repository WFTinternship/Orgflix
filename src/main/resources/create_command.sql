# clear database
drop table if exists lists;
drop table if exists film_to_director;
drop table if exists directors;
drop table if exists genre_to_film;
drop table if exists films;
drop table if exists users;
drop database if exists project;

# create database
create database project;
use project;

# create tables
create table users (
	ID int not null auto_increment primary key,
    Nick varchar(50) not null,
    User_Name varchar(250),
    User_Pass varchar(50) not null,
    Email varchar(50) not null unique
) engine = InnoDB;

create table films (
	ID int not null auto_increment primary key,
    Title varchar(250) not null,
    Prod_Year smallint,
    HasOscar boolean not null default false,
    Rate_1star int default 0,
    Rate_2star int default 0,
    Rate_3star int default 0,
    Rate_4star int default 0,
    Rate_5star int default 0
) engine = InnoDB;

create table genre (
  ID int not null PRIMARY key,
  genre varchar(50) not null
) engine = InnoDB;

create table genre_to_film (
	  Genre_ID int not null,
    Film_ID int not null,
    foreign key (Genre_ID) references genre(ID),
    foreign key (Film_ID) references films(ID)
) engine = InnoDB;

create table directors(
	ID int not null primary key auto_increment,
    Director_Name varchar(250) not null,
    HasOscar boolean not null default false
) engine = InnoDB;

create table film_to_director(
	Director_ID int not null,
    Film_ID int not null,
    foreign key (Director_ID) references directors(ID),
    foreign key (Film_ID) references films(ID)
) engine = InnoDB;

create table lists(
	User_ID int not null,
    Film_ID int not null,
    Is_watched boolean not null default false,
    Is_wished boolean not null default false,
    Is_public boolean not null default true,
    foreign key (User_ID) references users(ID),
    foreign key (Film_ID) references films(ID)
) engine = InnoDB;



# populating test data

# 2 users
insert into users(Nick,User_Name,User_Pass,Email)
   values('davit','Davit Abovyan','password1','davit.abovyan@gmail.com');
insert into users(Nick,User_Name,User_Pass,Email)
   values('karine','Karine','password2','karine@gmail.com');

# 2 directors
insert into directors(Director_Name,HasOscar)
   values('Quentin Tarantino',true);
insert into directors(Director_Name,HasOscar)
   values('Thomas Jahn',false);

# 2 films
insert into films(Title,Prod_Year,HasOscar)
   values('Pulp fiction',1994,true);
insert into films(Title,Prod_Year,HasOscar)
   values('Knocking on heavens door',1997,false);

# directors for 2 films
insert into film_to_director(Director_ID,Film_ID) values(1,1);
insert into film_to_director(Director_ID,Film_ID) values(2,1);
insert into film_to_director(Director_ID,Film_ID) values(2,2);

# 3 genre
insert into genre(ID,genre) values(1,'Crime');
insert into genre(ID,genre) values(2,'Comedy');
insert into genre(ID,genre) values(3,'Action');


# 2 genre to 2 films
insert into genre_to_film(Genre_ID,Film_ID) values(1,1);
insert into genre_to_film(Genre_ID,Film_ID) values(1,2);
insert into genre_to_film(Genre_ID,Film_ID) values(2,2);
insert into genre_to_film(Genre_ID,Film_ID) values(3,2);

