# FOR TEST DB
# CLEAR DATABASE
CREATE DATABASE IF NOT EXISTS ORGFLIXTEST;
USE ORGFLIXTEST;

# FOR REAL DB
# CLEAR DATABASE
CREATE DATABASE IF NOT EXISTS ORGFLIX;
USE ORGFLIX;
DROP TABLE IF EXISTS LISTS;
DROP TABLE IF EXISTS FILM_TO_CAST;
DROP TABLE IF EXISTS CASTS;
DROP TABLE IF EXISTS GENRE_TO_FILM;
DROP TABLE IF EXISTS GENRE;
DROP TABLE IF EXISTS FILMS;
DROP TABLE IF EXISTS USERS;


# GENERAL PART - RUN TWICE
# CREATE TABLES
CREATE TABLE USERS (
  ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  NICK VARCHAR(50) NOT NULL UNIQUE ,
  USER_NAME VARCHAR(250),
  USER_PASS VARCHAR(50) NOT NULL,
  EMAIL VARCHAR(50) NOT NULL UNIQUE
) ENGINE = INNODB;

CREATE TABLE FILMS (
  ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  TITLE VARCHAR(250) NOT NULL,
  PROD_YEAR SMALLINT NOT NULL ,
  HAS_OSCAR BOOLEAN NOT NULL DEFAULT FALSE,
  IMAGE_REF VARCHAR(250),
  DIRECTOR VARCHAR(250),
  RATE_1STAR INT DEFAULT 0,
  RATE_2STAR INT DEFAULT 0,
  RATE_3STAR INT DEFAULT 0,
  RATE_4STAR INT DEFAULT 0,
  RATE_5STAR INT DEFAULT 0
) ENGINE = INNODB;

CREATE TABLE GENRE (
  ID INT NOT NULL PRIMARY KEY,
  GENRE VARCHAR(50) NOT NULL
) ENGINE = INNODB;

CREATE TABLE GENRE_TO_FILM (
  GENRE_ID INT NOT NULL,
  FILM_ID INT NOT NULL,
  FOREIGN KEY (GENRE_ID) REFERENCES GENRE(ID),
  FOREIGN KEY (FILM_ID) REFERENCES FILMS(ID)
) ENGINE = INNODB;

CREATE TABLE CASTS(
  ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  ACTOR_NAME VARCHAR(250) NOT NULL,
  HAS_OSCAR BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE = INNODB;

CREATE TABLE FILM_TO_CAST(
  ACTOR_ID INT NOT NULL,
  FILM_ID INT NOT NULL,
  FOREIGN KEY (ACTOR_ID) REFERENCES CASTS(ID),
  FOREIGN KEY (FILM_ID) REFERENCES FILMS(ID)
) ENGINE = INNODB;

CREATE TABLE LISTS(
  USER_ID INT NOT NULL,
  FILM_ID INT NOT NULL,
  IS_WATCHED BOOLEAN NOT NULL DEFAULT FALSE,
  IS_WISHED BOOLEAN NOT NULL DEFAULT FALSE,
  IS_PUBLIC BOOLEAN NOT NULL DEFAULT TRUE,
  FOREIGN KEY (USER_ID) REFERENCES USERS(ID),
  FOREIGN KEY (FILM_ID) REFERENCES FILMS(ID)
) ENGINE = INNODB;



# POPULATING TEST DATA

# 2 films
insert into films(Title,Prod_Year,Has_Oscar,image_ref)
   values('The Shawshank Redemption',1994,true,'1/1.jpg');
insert into casts(Actor_Name,Has_Oscar)
  values('Tim Robbins',true);
insert into casts(Actor_Name,Has_Oscar)
  values('Morgan Freeman',false);
insert into film_to_cast(Actor_ID,Film_ID) values(1,1);
insert into film_to_cast(Actor_ID,Film_ID) values(2,1);

insert into films(Title,Prod_Year,Has_Oscar,image_ref)
   values('The Godfather',1972,true,'2/2.jpg');
insert into casts(Actor_Name,Has_Oscar)
  values('Marlon Brando',true);
insert into casts(Actor_Name,Has_Oscar)
  values('Al Pacino',false);
insert into film_to_cast(Actor_ID,Film_ID) values(3,2);
insert into film_to_cast(Actor_ID,Film_ID) values(4,2);


insert into films(Title,Prod_Year,Has_Oscar,image_ref)
  values('The Godfather: Part II',1974,true,'3/3.jpg');
insert into casts(Actor_Name,Has_Oscar)
  values('Robert De Niro',true);
insert into film_to_cast(Actor_ID,Film_ID) values(4,3);
insert into film_to_cast(Actor_ID,Film_ID) values(5,3);


insert into films(Title,Prod_Year,Has_Oscar,image_ref)
  values('The Dark Knight',2008,false,'4/4.jpg');
insert into casts(Actor_Name,Has_Oscar)
  values('Christian Bale',true);
insert into casts(Actor_Name,Has_Oscar)
  values('Heath Ledger',false);
insert into film_to_cast(Actor_ID,Film_ID) values(6,4);
insert into film_to_cast(Actor_ID,Film_ID) values(7,4);


insert into films(Title,Prod_Year,Has_Oscar,image_ref)
  values('12 Angry Men',1957,false,'5/5.jpg');
insert into casts(Actor_Name,Has_Oscar)
  values('Henry Fonda',true);
insert into casts(Actor_Name,Has_Oscar)
  values('Lee J. Cobb',false);
insert into film_to_cast(Actor_ID,Film_ID) values(8,5);
insert into film_to_cast(Actor_ID,Film_ID) values(9,5);

insert into films(Title,Prod_Year,Has_Oscar,image_ref)
  values('Schindlers List',1993,true,'6/6.jpg');
insert into casts(Actor_Name,Has_Oscar)
  values('Liam Neeson',true);
insert into casts(Actor_Name,Has_Oscar)
  values('Ralph Fiennes',false);
insert into film_to_cast(Actor_ID,Film_ID) values(10,6);
insert into film_to_cast(Actor_ID,Film_ID) values(11,6);

insert into films(Title,Prod_Year,Has_Oscar,image_ref)
  values('Pulp Fiction',1994,true,'7/7.jpg');
insert into casts(Actor_Name,Has_Oscar)
  values('John Travolta',true);
insert into casts(Actor_Name,Has_Oscar)
  values('Samuel L. Jackson',false);
insert into film_to_cast(Actor_ID,Film_ID) values(12,7);
insert into film_to_cast(Actor_ID,Film_ID) values(13,7);

insert into films(Title,Prod_Year,Has_Oscar,image_ref)
  values('The Lord of the Rings: The Return of the King',2003,true,'8/8.jpg');
insert into casts(Actor_Name,Has_Oscar)
  values('Elijah Wood',true);
insert into casts(Actor_Name,Has_Oscar)
  values('Viggo Mortensen',false);
insert into film_to_cast(Actor_ID,Film_ID) values(14,8);
insert into film_to_cast(Actor_ID,Film_ID) values(15,8);

insert into films(Title,Prod_Year,Has_Oscar,image_ref)
  values('The Good, the Bad and the Ugly',1996,false,'9/9.jpg');
insert into casts(Actor_Name,Has_Oscar)
  values('Clint Eastwood',true);
insert into casts(Actor_Name,Has_Oscar)
  values('Eli Wallach',false);
insert into film_to_cast(Actor_ID,Film_ID) values(16,9);
insert into film_to_cast(Actor_ID,Film_ID) values(17,9);

insert into films(Title,Prod_Year,Has_Oscar,image_ref)
  values('Fight Club',1999,false,'10/10.jpg');
insert into casts(Actor_Name,Has_Oscar)
  values('Brad Pitt',true);
insert into casts(Actor_Name,Has_Oscar)
  values('Edward Norton',false);
insert into film_to_cast(Actor_ID,Film_ID) values(18,10);
insert into film_to_cast(Actor_ID,Film_ID) values(19,10);

insert into films(Title,Prod_Year,Has_Oscar,image_ref)
  values('The Lord of the Rings: The Fellowship of the Ring',2001,true,'11/11.jpg');
insert into casts(Actor_Name,Has_Oscar)
  values('Ian McKellen',true);
insert into film_to_cast(Actor_ID,Film_ID) values(14,11);
insert into film_to_cast(Actor_ID,Film_ID) values(19,11);

insert into films(Title,Prod_Year,Has_Oscar,image_ref)
values('Forrest Gump',1994,true,'12/12.jpg');
insert into casts(Actor_Name,Has_Oscar)
values('Tom Hanks',true);
insert into casts(Actor_Name,Has_Oscar)
values('Robin Wright',false);
insert into film_to_cast(Actor_ID,Film_ID) values(20,12);
insert into film_to_cast(Actor_ID,Film_ID) values(21,12);

insert into films(Title,Prod_Year,Has_Oscar,image_ref)
  values('Star Wars: Episode V - The Empire Strikes Back',1980,true,'13/13.jpg');
insert into casts(Actor_Name,Has_Oscar)
  values('Mark Hamill',true);
insert into casts(Actor_Name,Has_Oscar)
  values('Harrison Ford',false);
insert into film_to_cast(Actor_ID,Film_ID) values(22,13);
insert into film_to_cast(Actor_ID,Film_ID) values(23,13);



# 3 GENRE
INSERT INTO GENRE(ID, GENRE) VALUES (1, 'FAMILY');
INSERT INTO GENRE(ID, GENRE) VALUES (2, 'HISTORY');
INSERT INTO GENRE(ID, GENRE) VALUES (3, 'MUSIC');
INSERT INTO GENRE(ID, GENRE) VALUES (4, 'MYSTERY');
INSERT INTO GENRE(ID, GENRE) VALUES (5, 'SCI_FI');
INSERT INTO GENRE(ID, GENRE) VALUES (6, 'THRILLER');
INSERT INTO GENRE(ID, GENRE) VALUES (7, 'WESTERN');
INSERT INTO GENRE(ID, GENRE) VALUES (8, 'ACTION');
INSERT INTO GENRE(ID, GENRE) VALUES (9, 'ANIMATION');
INSERT INTO GENRE(ID, GENRE) VALUES (10, 'COMEDY');
INSERT INTO GENRE(ID, GENRE) VALUES (11, 'DOCUMENTARY');
INSERT INTO GENRE(ID, GENRE) VALUES (12, 'ADVENTURE');
INSERT INTO GENRE(ID, GENRE) VALUES (13, 'BIOGRAPHY');
INSERT INTO GENRE(ID, GENRE) VALUES (14, 'CRIME');
INSERT INTO GENRE(ID, GENRE) VALUES (15, 'DRAMA');
INSERT INTO GENRE(ID, GENRE) VALUES (16, 'FANTASY');
INSERT INTO GENRE(ID, GENRE) VALUES (17, 'HORROR');
INSERT INTO GENRE(ID, GENRE) VALUES (18, 'MUSICAL');
INSERT INTO GENRE(ID, GENRE) VALUES (19, 'ROMANCE');
INSERT INTO GENRE(ID, GENRE) VALUES (20, 'SPORT');
INSERT INTO GENRE(ID, GENRE) VALUES (21, 'WAR');


# # 2 GENRE TO 2 FILMS
# INSERT INTO GENRE_TO_FILM(GENRE_ID,FILM_ID) VALUES(1,1);
# INSERT INTO GENRE_TO_FILM(GENRE_ID,FILM_ID) VALUES(1,2);
# INSERT INTO GENRE_TO_FILM(GENRE_ID,FILM_ID) VALUES(2,2);
# INSERT INTO GENRE_TO_FILM(GENRE_ID,FILM_ID) VALUES(3,2);
#
