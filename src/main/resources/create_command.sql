# For TEST DB
# clear database
CREATE DATABASE IF NOT EXISTS orgflixtest;
USE orgflixtest;

# For REAL DB
# clear database
CREATE DATABASE IF NOT EXISTS orgflix;
USE orgflix;
DROP TABLE IF EXISTS lists;
DROP TABLE IF EXISTS film_to_cast;
DROP TABLE IF EXISTS casts;
DROP TABLE IF EXISTS genre_to_film;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS users;

# General part - run twice
# create tables
CREATE TABLE users (
  ID        INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Nick      VARCHAR(50) NOT NULL,
  User_Name VARCHAR(250),
  User_Pass VARCHAR(50) NOT NULL,
  Email     VARCHAR(50) NOT NULL UNIQUE
)
  ENGINE = InnoDB;

CREATE TABLE films (
  ID         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Title      VARCHAR(250) NOT NULL,
  Prod_Year  SMALLINT     NOT NULL,
  HasOscar   BOOLEAN      NOT NULL DEFAULT FALSE,
  image_ref  VARCHAR(250),
  Director   VARCHAR(250),
  Rate_1star INT                   DEFAULT 0,
  Rate_2star INT                   DEFAULT 0,
  Rate_3star INT                   DEFAULT 0,
  Rate_4star INT                   DEFAULT 0,
  Rate_5star INT                   DEFAULT 0
)
  ENGINE = InnoDB;

CREATE TABLE genre (
  ID    INT         NOT NULL PRIMARY KEY,
  genre VARCHAR(50) NOT NULL
)
  ENGINE = InnoDB;

CREATE TABLE genre_to_film (
  Genre_ID INT NOT NULL,
  Film_ID  INT NOT NULL,
  FOREIGN KEY (Genre_ID) REFERENCES genre (ID),
  FOREIGN KEY (Film_ID) REFERENCES films (ID)
)
  ENGINE = InnoDB;

CREATE TABLE casts (
  ID         INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
  Actor_Name VARCHAR(250) NOT NULL,
  HasOscar   BOOLEAN      NOT NULL             DEFAULT FALSE
)
  ENGINE = InnoDB;

CREATE TABLE film_to_cast (
  Actor_ID INT NOT NULL,
  Film_ID  INT NOT NULL,
  FOREIGN KEY (Actor_ID) REFERENCES casts (ID),
  FOREIGN KEY (Film_ID) REFERENCES films (ID)
)
  ENGINE = InnoDB;

CREATE TABLE Lists (
  User_ID    INT     NOT NULL,
  Film_ID    INT     NOT NULL,
  Is_watched BOOLEAN NOT NULL DEFAULT FALSE,
  Is_wished  BOOLEAN NOT NULL DEFAULT FALSE,
  Is_public  BOOLEAN NOT NULL DEFAULT TRUE,
  FOREIGN KEY (User_ID) REFERENCES users (ID),
  FOREIGN KEY (Film_ID) REFERENCES films (ID)
)
  ENGINE = InnoDB;

# populating test data

# 2 films
INSERT INTO films (Title, Prod_Year, HasOscar, image_ref)
VALUES ('The Shawshank Redemption', 1994, TRUE, 'images/12345.jpg');
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Tim Robbins', TRUE);
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Morgan Freeman', FALSE);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (1, 1);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (2, 1);

INSERT INTO films (Title, Prod_Year, HasOscar, image_ref)
VALUES ('The Godfather', 1972, TRUE, 'images/23452.jpg');
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Marlon Brando', TRUE);
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Al Pacino', FALSE);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (3, 2);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (4, 2);


INSERT INTO films (Title, Prod_Year, HasOscar, image_ref)
VALUES ('The Godfather: Part II', 1974, TRUE, 'images/34555.jpg');
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Robert De Niro', TRUE);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (4, 3);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (5, 3);


INSERT INTO films (Title, Prod_Year, HasOscar, image_ref)
VALUES ('The Dark Knight', 2008, FALSE, 'images/72623.jpg');
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Christian Bale', TRUE);
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Heath Ledger', FALSE);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (6, 4);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (7, 4);


INSERT INTO films (Title, Prod_Year, HasOscar, image_ref)
VALUES ('12 Angry Men', 1957, FALSE, 'images/53234.jpg');
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Henry Fonda', TRUE);
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Lee J. Cobb', FALSE);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (8, 5);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (9, 5);

INSERT INTO films (Title, Prod_Year, HasOscar, image_ref)
VALUES ('Schindlers List', 1993, TRUE, 'images/86523.jpg');
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Liam Neeson', TRUE);
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Ralph Fiennes', FALSE);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (10, 6);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (11, 6);

INSERT INTO films (Title, Prod_Year, HasOscar, image_ref)
VALUES ('Pulp Fiction', 1994, TRUE, 'images/45352.jpg');
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('John Travolta', TRUE);
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Samuel L. Jackson', FALSE);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (12, 7);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (13, 7);

INSERT INTO films (Title, Prod_Year, HasOscar, image_ref)
VALUES ('The Lord of the Rings: The Return of the King', 2003, TRUE, 'images/98343.jpg');
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Elijah Wood', TRUE);
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Viggo Mortensen', FALSE);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (14, 8);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (15, 8);

INSERT INTO films (Title, Prod_Year, HasOscar, image_ref)
VALUES ('The Good, the Bad and the Ugly', 1996, FALSE, 'images/12923.jpg');
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Clint Eastwood', TRUE);
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Eli Wallach', FALSE);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (16, 9);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (17, 9);

INSERT INTO films (Title, Prod_Year, HasOscar, image_ref)
VALUES ('Fight Club', 1999, FALSE, 'images/76765.jpg');
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Brad Pitt', TRUE);
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Edward Norton', FALSE);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (18, 10);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (19, 10);

INSERT INTO films (Title, Prod_Year, HasOscar, image_ref)
VALUES ('The Lord of the Rings: The Fellowship of the Ring', 2001, TRUE, 'images/89733.jpg');
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Ian McKellen', TRUE);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (14, 11);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (19, 11);

INSERT INTO films (Title, Prod_Year, HasOscar, image_ref)
VALUES ('Forrest Gump', 1994, TRUE, 'images/54545.jpg');
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Tom Hanks', TRUE);
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Robin Wright', FALSE);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (20, 12);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (21, 12);

INSERT INTO films (Title, Prod_Year, HasOscar, image_ref)
VALUES ('Star Wars: Episode V - The Empire Strikes Back', 1980, TRUE, 'images/99778.jpg');
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Mark Hamill', TRUE);
INSERT INTO casts (Actor_Name, HasOscar)
VALUES ('Harrison Ford', FALSE);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (22, 13);
INSERT INTO film_to_cast (Actor_ID, Film_ID) VALUES (23, 13);

# 3 genre
INSERT INTO genre (id, genre) VALUES (1, 'FAMILY');
INSERT INTO genre (id, genre) VALUES (2, 'HISTORY');
INSERT INTO genre (id, genre) VALUES (3, 'MUSIC');
INSERT INTO genre (id, genre) VALUES (4, 'MYSTERY');
INSERT INTO genre (id, genre) VALUES (5, 'SCI_FI');
INSERT INTO genre (id, genre) VALUES (6, 'THRILLER');
INSERT INTO genre (id, genre) VALUES (7, 'WESTERN');
INSERT INTO genre (id, genre) VALUES (8, 'ACTION');
INSERT INTO genre (id, genre) VALUES (9, 'ANIMATION');
INSERT INTO genre (id, genre) VALUES (10, 'COMEDY');
INSERT INTO genre (id, genre) VALUES (11, 'DOCUMENTARY');
INSERT INTO genre (id, genre) VALUES (12, 'ADVENTURE');
INSERT INTO genre (id, genre) VALUES (13, 'BIOGRAPHY');
INSERT INTO genre (id, genre) VALUES (14, 'CRIME');
INSERT INTO genre (id, genre) VALUES (15, 'DRAMA');
INSERT INTO genre (id, genre) VALUES (16, 'FANTASY');
INSERT INTO genre (id, genre) VALUES (17, 'HORROR');
INSERT INTO genre (id, genre) VALUES (18, 'MUSICAL');
INSERT INTO genre (id, genre) VALUES (19, 'ROMANCE');
INSERT INTO genre (id, genre) VALUES (20, 'SPORT');
INSERT INTO genre (id, genre) VALUES (21, 'WAR');

# # 2 genre to 2 films
# insert into genre_to_film(Genre_ID,Film_ID) values(1,1);
# insert into genre_to_film(Genre_ID,Film_ID) values(1,2);
# insert into genre_to_film(Genre_ID,Film_ID) values(2,2);
# insert into genre_to_film(Genre_ID,Film_ID) values(3,2);
#
