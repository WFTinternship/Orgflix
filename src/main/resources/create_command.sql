# clear database
drop table if exists lists;
drop table if exists film_to_director;
drop table if exists casts;
drop table if exists genre_to_film;
drop table if exists films;
drop table if exists users;
drop database if exists orgflix;

# create database
create database orgflix;

use orgflix;

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

# # 2 users
# insert into users(Nick,User_Name,User_Pass,Email)
#    values('davit','Davit Abovyan','password1','davit.abovyan@gmail.com');
# insert into users(Nick,User_Name,User_Pass,Email)
#    values('karine','Karine','password2','karine@gmail.com');
#
# # 2 casts
# insert into casts(Actor_Name,HasOscar)
#    values('Quentin Tarantino',true);
# insert into casts(Actor_Name,HasOscar)
#    values('Thomas Jahn',false);
#
# # 2 films
# insert into films(Title,Prod_Year,HasOscar,image_ref)
#    values('The Shawshank Redemption',1994,true,'12345');
# insert into films(Title,Prod_Year,HasOscar,image_ref)
#    values('The Godfather',1972,true,'23452');
# insert into films(Title,Prod_Year,HasOscar,image_ref)
#   values('The Godfather: Part II',1974,true,'34555');
# insert into films(Title,Prod_Year,HasOscar,image_ref)
#   values('The Dark Knight',2008,false,'72623');
# insert into films(Title,Prod_Year,HasOscar,image_ref)
#   values('12 Angry Men',1957,false,'53234');
# insert into films(Title,Prod_Year,HasOscar,image_ref)
#   values('Schindlers List',1993,true,'86523');
# insert into films(Title,Prod_Year,HasOscar,image_ref)
#   values('Pulp Fiction',1994,true,'45352');
# insert into films(Title,Prod_Year,HasOscar,image_ref)
#   values('The Lord of the Rings: The Return of the King',2003,true,'98343');
# insert into films(Title,Prod_Year,HasOscar,image_ref)
#   values('The Good, the Bad and the Ugly',1996,false,'12923');
# insert into films(Title,Prod_Year,HasOscar,image_ref)
#   values('Fight Club',1999,false,'76765');
# insert into films(Title,Prod_Year,HasOscar,image_ref)
#   values('The Lord of the Rings: The Fellowship of the Ring',2001,true,'89733');
# insert into films(Title,Prod_Year,HasOscar,image_ref)
#   values('Star Wars: Episode V - The Empire Strikes Back',1980,true,'99778');
# insert into films(Title,Prod_Year,HasOscar,image_ref)
#   values('Forrest Gump',1994,true,'54545');
#
# # casts for 2 films
# insert into film_to_cast(Actor_ID,Film_ID) values(1,1);
# insert into film_to_cast(Actor_ID,Film_ID) values(2,1);
# insert into film_to_cast(Actor_ID,Film_ID) values(2,2);

# 3 genre
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


# # 2 genre to 2 films
# insert into genre_to_film(Genre_ID,Film_ID) values(1,1);
# insert into genre_to_film(Genre_ID,Film_ID) values(1,2);
# insert into genre_to_film(Genre_ID,Film_ID) values(2,2);
# insert into genre_to_film(Genre_ID,Film_ID) values(3,2);
#
