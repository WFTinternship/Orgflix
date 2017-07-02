# For TEST DB
# clear database
create database if NOT EXISTS orgflixtest;
use orgflixtest;

# For REAL DB
# clear database
create database if NOT EXISTS orgflix;
use orgflix;
drop table if exists lists;
drop table if exists film_to_cast;
drop table if exists casts;
drop table if exists genre_to_film;
drop table if exists genre;
drop table if exists films;
drop table if exists users;


# Genreal part - run twice
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



# populating test data

# 2 films
insert into films(Title,Prod_Year,HasOscar,image_ref)
   values('The Shawshank Redemption',1994,true,'images/12345.jpg');
insert into casts(Actor_Name,HasOscar)
  values('Tim Robbins',true);
insert into casts(Actor_Name,HasOscar)
  values('Morgan Freeman',false);
insert into film_to_cast(Actor_ID,Film_ID) values(1,1);
insert into film_to_cast(Actor_ID,Film_ID) values(2,1);

insert into films(Title,Prod_Year,HasOscar,image_ref)
   values('The Godfather',1972,true,'images/23452.jpg');
insert into casts(Actor_Name,HasOscar)
  values('Marlon Brando',true);
insert into casts(Actor_Name,HasOscar)
  values('Al Pacino',false);
insert into film_to_cast(Actor_ID,Film_ID) values(3,2);
insert into film_to_cast(Actor_ID,Film_ID) values(4,2);


insert into films(Title,Prod_Year,HasOscar,image_ref)
  values('The Godfather: Part II',1974,true,'images/34555.jpg');
insert into casts(Actor_Name,HasOscar)
  values('Robert De Niro',true);
insert into film_to_cast(Actor_ID,Film_ID) values(4,3);
insert into film_to_cast(Actor_ID,Film_ID) values(5,3);


insert into films(Title,Prod_Year,HasOscar,image_ref)
  values('The Dark Knight',2008,false,'images/72623.jpg');
insert into casts(Actor_Name,HasOscar)
  values('Christian Bale',true);
insert into casts(Actor_Name,HasOscar)
  values('Heath Ledger',false);
insert into film_to_cast(Actor_ID,Film_ID) values(6,4);
insert into film_to_cast(Actor_ID,Film_ID) values(7,4);


insert into films(Title,Prod_Year,HasOscar,image_ref)
  values('12 Angry Men',1957,false,'images/53234.jpg');
insert into casts(Actor_Name,HasOscar)
  values('Henry Fonda',true);
insert into casts(Actor_Name,HasOscar)
  values('Lee J. Cobb',false);
insert into film_to_cast(Actor_ID,Film_ID) values(8,5);
insert into film_to_cast(Actor_ID,Film_ID) values(9,5);

insert into films(Title,Prod_Year,HasOscar,image_ref)
  values('Schindlers List',1993,true,'images/86523.jpg');
insert into casts(Actor_Name,HasOscar)
  values('Liam Neeson',true);
insert into casts(Actor_Name,HasOscar)
  values('Ralph Fiennes',false);
insert into film_to_cast(Actor_ID,Film_ID) values(10,6);
insert into film_to_cast(Actor_ID,Film_ID) values(11,6);

insert into films(Title,Prod_Year,HasOscar,image_ref)
  values('Pulp Fiction',1994,true,'images/45352.jpg');
insert into casts(Actor_Name,HasOscar)
  values('John Travolta',true);
insert into casts(Actor_Name,HasOscar)
  values('Samuel L. Jackson',false);
insert into film_to_cast(Actor_ID,Film_ID) values(12,7);
insert into film_to_cast(Actor_ID,Film_ID) values(13,7);

insert into films(Title,Prod_Year,HasOscar,image_ref)
  values('The Lord of the Rings: The Return of the King',2003,true,'images/98343.jpg');
insert into casts(Actor_Name,HasOscar)
  values('Elijah Wood',true);
insert into casts(Actor_Name,HasOscar)
  values('Viggo Mortensen',false);
insert into film_to_cast(Actor_ID,Film_ID) values(14,8);
insert into film_to_cast(Actor_ID,Film_ID) values(15,8);

insert into films(Title,Prod_Year,HasOscar,image_ref)
  values('The Good, the Bad and the Ugly',1996,false,'images/12923.jpg');
insert into casts(Actor_Name,HasOscar)
  values('Clint Eastwood',true);
insert into casts(Actor_Name,HasOscar)
  values('Eli Wallach',false);
insert into film_to_cast(Actor_ID,Film_ID) values(16,9);
insert into film_to_cast(Actor_ID,Film_ID) values(17,9);

insert into films(Title,Prod_Year,HasOscar,image_ref)
  values('Fight Club',1999,false,'images/76765.jpg');
insert into casts(Actor_Name,HasOscar)
  values('Brad Pitt',true);
insert into casts(Actor_Name,HasOscar)
  values('Edward Norton',false);
insert into film_to_cast(Actor_ID,Film_ID) values(18,10);
insert into film_to_cast(Actor_ID,Film_ID) values(19,10);

insert into films(Title,Prod_Year,HasOscar,image_ref)
  values('The Lord of the Rings: The Fellowship of the Ring',2001,true,'images/89733.jpg');
insert into casts(Actor_Name,HasOscar)
  values('Ian McKellen',true);
insert into film_to_cast(Actor_ID,Film_ID) values(14,11);
insert into film_to_cast(Actor_ID,Film_ID) values(19,11);

insert into films(Title,Prod_Year,HasOscar,image_ref)
values('Forrest Gump',1994,true,'images/54545.jpg');
insert into casts(Actor_Name,HasOscar)
values('Tom Hanks',true);
insert into casts(Actor_Name,HasOscar)
values('Robin Wright',false);
insert into film_to_cast(Actor_ID,Film_ID) values(20,12);
insert into film_to_cast(Actor_ID,Film_ID) values(21,12);

insert into films(Title,Prod_Year,HasOscar,image_ref)
  values('Star Wars: Episode V - The Empire Strikes Back',1980,true,'images/99778.jpg');
insert into casts(Actor_Name,HasOscar)
  values('Mark Hamill',true);
insert into casts(Actor_Name,HasOscar)
  values('Harrison Ford',false);
insert into film_to_cast(Actor_ID,Film_ID) values(22,13);
insert into film_to_cast(Actor_ID,Film_ID) values(23,13);



# 3 genre
INSERT INTO genre(id, genre) VALUES (1, 'FAMILY');
INSERT INTO genre(id, genre) VALUES (2, 'HISTORY');
INSERT INTO genre(id, genre) VALUES (3, 'MUSIC');
INSERT INTO genre(id, genre) VALUES (4, 'MYSTERY');
INSERT INTO genre(id, genre) VALUES (5, 'SCI_FI');
INSERT INTO genre(id, genre) VALUES (6, 'THRILLER');
INSERT INTO genre(id, genre) VALUES (7, 'WESTERN');
INSERT INTO genre(id, genre) VALUES (8, 'ACTION');
INSERT INTO genre(id, genre) VALUES (9, 'ANIMATION');
INSERT INTO genre(id, genre) VALUES (10, 'COMEDY');
INSERT INTO genre(id, genre) VALUES (11, 'DOCUMENTARY');
INSERT INTO genre(id, genre) VALUES (12, 'ADVENTURE');
INSERT INTO genre(id, genre) VALUES (13, 'BIOGRAPHY');
INSERT INTO genre(id, genre) VALUES (14, 'CRIME');
INSERT INTO genre(id, genre) VALUES (15, 'DRAMA');
INSERT INTO genre(id, genre) VALUES (16, 'FANTASY');
INSERT INTO genre(id, genre) VALUES (17, 'HORROR');
INSERT INTO genre(id, genre) VALUES (18, 'MUSICAL');
INSERT INTO genre(id, genre) VALUES (19, 'ROMANCE');
INSERT INTO genre(id, genre) VALUES (20, 'SPORT');
INSERT INTO genre(id, genre) VALUES (21, 'WAR');


# # 2 genre to 2 films
# insert into genre_to_film(Genre_ID,Film_ID) values(1,1);
# insert into genre_to_film(Genre_ID,Film_ID) values(1,2);
# insert into genre_to_film(Genre_ID,Film_ID) values(2,2);
# insert into genre_to_film(Genre_ID,Film_ID) values(3,2);
#
