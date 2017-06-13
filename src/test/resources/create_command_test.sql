# clear database
drop table if exists lists;
drop table if exists film_to_director;
drop table if exists casts;
drop table if exists genre_to_film;
drop table if exists films;
drop table if exists users;
drop database if exists orgflix_test;

# create database
create database orgflix_test;

use orgflix_test;

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
  Prod_Year smallint not NULL ,
  HasOscar boolean not null default false,
  image_ref varchar(250),
  Director VARCHAR(250),
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

create table casts(
  ID int not null primary key auto_increment,
  Actor_Name varchar(250) not null,
  HasOscar boolean not null default false
) engine = InnoDB;

create table film_to_cast(
  Actor_ID int not null,
  Film_ID int not null,
  foreign key (Actor_ID) references casts(ID),
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


INSERT INTO genre(id, genre) VALUES (0, 'FAMILY');
INSERT INTO genre(id, genre) VALUES (1, 'HISTORY');
INSERT INTO genre(id, genre) VALUES (2, 'MUSIC');
INSERT INTO genre(id, genre) VALUES (3, 'MYSTERY');
INSERT INTO genre(id, genre) VALUES (4, 'SCI_FI');
INSERT INTO genre(id, genre) VALUES (5, 'THRILLER');
INSERT INTO genre(id, genre) VALUES (6, 'WESTERN');
INSERT INTO genre(id, genre) VALUES (7, 'ACTION');
INSERT INTO genre(id, genre) VALUES (8, 'ANIMATION');
INSERT INTO genre(id, genre) VALUES (9, 'COMEDY');
INSERT INTO genre(id, genre) VALUES (10, 'DOCUMENTARY');
INSERT INTO genre(id, genre) VALUES (11, 'ADVENTURE');
INSERT INTO genre(id, genre) VALUES (12, 'BIOGRAPHY');
INSERT INTO genre(id, genre) VALUES (13, 'CRIME');
INSERT INTO genre(id, genre) VALUES (14, 'DRAMA');
INSERT INTO genre(id, genre) VALUES (15, 'FANTASY');
INSERT INTO genre(id, genre) VALUES (16, 'HORROR');
INSERT INTO genre(id, genre) VALUES (17, 'MUSICAL');
INSERT INTO genre(id, genre) VALUES (18, 'ROMANCE');
INSERT INTO genre(id, genre) VALUES (19, 'SPORT');
INSERT INTO genre(id, genre) VALUES (20, 'WAR');


