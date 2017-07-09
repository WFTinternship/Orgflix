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

# 2 FILMS
INSERT INTO FILMS(TITLE,PROD_YEAR,HAS_OSCAR,IMAGE_REF)
VALUES('The Shawshank Redemption',1994,TRUE,'IMAGES/12345.JPG');
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Tim Robbins',TRUE);
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Morgan Freeman',FALSE);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(1,1);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(2,1);

INSERT INTO FILMS(TITLE,PROD_YEAR,HAS_OSCAR,IMAGE_REF)
VALUES('The Godfather',1972,TRUE,'IMAGES/23452.JPG');
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Marlon Brando',TRUE);
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Al Pacino',FALSE);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(3,2);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(4,2);


INSERT INTO FILMS(TITLE,PROD_YEAR,HAS_OSCAR,IMAGE_REF)
VALUES('The Godfather: Part II',1974,TRUE,'IMAGES/34555.JPG');
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Robert De Niro',TRUE);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(4,3);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(5,3);


INSERT INTO FILMS(TITLE,PROD_YEAR,HAS_OSCAR,IMAGE_REF)
VALUES('The Dark Knight',2008,FALSE,'IMAGES/72623.JPG');
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Christian Bale',TRUE);
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Heath Ledger',FALSE);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(6,4);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(7,4);


INSERT INTO FILMS(TITLE,PROD_YEAR,HAS_OSCAR,IMAGE_REF)
VALUES('12 Angry Men',1957,FALSE,'IMAGES/53234.JPG');
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Henry Fonda',TRUE);
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Lee J. Cobb',FALSE);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(8,5);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(9,5);

INSERT INTO FILMS(TITLE,PROD_YEAR,HAS_OSCAR,IMAGE_REF)
VALUES('Schindlers List',1993,TRUE,'IMAGES/86523.JPG');
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Liam Neeson',TRUE);
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Ralph Fiennes',FALSE);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(10,6);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(11,6);

INSERT INTO FILMS(TITLE,PROD_YEAR,HAS_OSCAR,IMAGE_REF)
VALUES('Pulp Fiction',1994,TRUE,'IMAGES/45352.JPG');
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('John Travolta',TRUE);
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Samuel L. Jackson',FALSE);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(12,7);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(13,7);

INSERT INTO FILMS(TITLE,PROD_YEAR,HAS_OSCAR,IMAGE_REF)
VALUES('The Lord Of The Rings: The Return Of The King',2003,TRUE,'IMAGES/98343.JPG');
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Elijah Wood',TRUE);
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Viggo Mortensen',FALSE);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(14,8);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(15,8);

INSERT INTO FILMS(TITLE,PROD_YEAR,HAS_OSCAR,IMAGE_REF)
VALUES('The Good, The Bad And The Ugly',1996,FALSE,'IMAGES/12923.JPG');
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Clint Eastwood',TRUE);
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Eli Wallach',FALSE);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(16,9);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(17,9);

INSERT INTO FILMS(TITLE,PROD_YEAR,HAS_OSCAR,IMAGE_REF)
VALUES('Fight Club',1999,FALSE,'IMAGES/76765.JPG');
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Brad Pitt',TRUE);
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Edward Norton',FALSE);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(18,10);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(19,10);

INSERT INTO FILMS(TITLE,PROD_YEAR,HAS_OSCAR,IMAGE_REF)
VALUES('The Lord Of The Rings: The Fellowship Of The Ring',2001,TRUE,'IMAGES/89733.JPG');
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Ian Mckellen',TRUE);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(14,11);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(19,11);

INSERT INTO FILMS(TITLE,PROD_YEAR,HAS_OSCAR,IMAGE_REF)
VALUES('Forrest Gump',1994,TRUE,'IMAGES/54545.JPG');
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Tom Hanks',TRUE);
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Robin Wright',FALSE);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(20,12);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(21,12);

INSERT INTO FILMS(TITLE,PROD_YEAR,HAS_OSCAR,IMAGE_REF)
VALUES('Star Wars: Episode V - The Empire Strikes Back',1980,TRUE,'IMAGES/99778.JPG');
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Mark Hamill',TRUE);
INSERT INTO CASTS(ACTOR_NAME,HAS_OSCAR)
VALUES('Harrison Ford',FALSE);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(22,13);
INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES(23,13);



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
