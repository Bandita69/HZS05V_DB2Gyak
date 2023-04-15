

-- mysql create table about director

CREATE TABLE `directors` (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `name` VARCHAR2(255) NOT NULL,
  `born` INTEGER(4) NOT NULL,
  `gender` VARCHAR2(255) NOT NULL,
  `country` VARCHAR2(255) NOT NULL,
  CHECK (born >= 1900 AND born <= 2022)
);

-- mysql create table about studios

CREATE TABLE `studios` (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `name` VARCHAR2(255) NOT NULL,
  `founded` INTEGER(4) NOT NULL,
  `country` VARCHAR2(255) NOT NULL,
  CHECK (founded >= 1800 AND founded <= 2022)
);

-- mysql create table about movies

CREATE TABLE `movies` (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `title` VARCHAR2(255) NOT NULL,
  `year` INTEGER(4) NOT NULL,
  `director` INTEGER(10) NOT NULL,
  `genre` VARCHAR2(255) NOT NULL,
  `rating` INTEGER(2) NOT NULL,
  'studio' INTEGER(10) NOT NULL,
  FOREIGN KEY (studio) REFERENCES studios(id),
  FOREIGN KEY (director) REFERENCES directors(id),
  CHECK (rating >= 0 AND rating <= 10),
  CHECK (year >= 1900 AND year <= 2022)
);

-- mysql create table about actors

CREATE TABLE `actors` (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `name` VARCHAR2(255) NOT NULL,
  `born` INTEGER(4) NOT NULL,
  `gender` VARCHAR2(255) NOT NULL,
  `country` VARCHAR2(255) NOT NULL,
  CHECK (born >= 1900 AND born <= 2022)
);

-- mysql create table about movieActors

CREATE TABLE 'movieActors' (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    'movie_id' INTEGER,
    'actor_id' INTEGER,
    FOREIGN KEY (actor_id) REFERENCES actors(id),
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);

-- mysql create table about reviews

CREATE TABLE `reviews` (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `movie_id` INTEGER(10) NOT NULL,
  `review` VARCHAR2(255),
  `rating` INTEGER(2) NOT NULL,
  `date` DATE NOT NULL,
  FOREIGN KEY (movie_id) REFERENCES movies(id),
  CHECK (rating >= 0 AND rating <= 10)
);



-- inserts for directors

INSERT INTO directors (name, born, gender, country) VALUES ('Frank Darabont', 1959, 'male', 'France');
INSERT INTO directors (name, born, gender, country) VALUES ('Francis Ford Coppola', 1939, 'male', 'USA');
INSERT INTO directors (name, born, gender, country) VALUES ('Christopher Nolan', 1970, 'male', 'UK');

-- inserts for studios
INSERT INTO studios (name, founded, country) VALUES ('Castle Rock Entertainment', 1987, 'USA');
INSERT INTO studios (name, founded, country) VALUES ('Paramount Pictures', 1912, 'USA');
INSERT INTO studios (name, founded, country) VALUES ('Warner Bros.', 1923, 'USA');

-- inserts for movies

INSERT INTO movies (title, year, director, genre, rating, studio) VALUES ('The Shawshank Redemption', 1994, 1, 'Drama', 9.2, 1);
INSERT INTO movies (title, year, director, genre, rating, studio) VALUES ('The Godfather', 1972, 2, 'Crime', 9.2, 2);
INSERT INTO movies (title, year, director, genre, rating, studio) VALUES ('The Godfather: Part II', 1974, 2, 'Crime', 9.0, 2);
INSERT INTO movies (title, year, director, genre, rating, studio) VALUES ('The Dark Knight', 2008, 3, 'Action', 9.0, 3);
INSERT INTO movies (title, year, director, genre, rating, studio) VALUES ('The Green Mile', 1999, 1, 'Drama', 8.6, 1);


-- inserts for actors
-- The Shawshank Redemption
INSERT INTO actors (name, born, gender, country)
VALUES ('Tim Robbins', 1958, 'male', 'USA'),
       ('Morgan Freeman', 1937, 'male', 'USA');

-- The Godfather
INSERT INTO actors (name, born, gender, country)
VALUES ('Marlon Brando', 1924, 'male', 'USA'),
       ('Al Pacino', 1940, 'male', 'USA');

-- The Godfather: Part II  alpacino 
INSERT INTO actors (name, born, gender, country)
VALUES ('Robert De Niro', 1943, 'male', 'USA');

-- The Dark Knight
INSERT INTO actors (name, born, gender, country)
VALUES ('Christian Bale', 1974, 'male', 'UK'),
       ('Heath Ledger', 1979, 'male', 'Australia');

-- The Green Mile
INSERT INTO actors (name, born, gender, country)
VALUES ('Tom Hanks', 1956, 'male', 'USA'),
       ('Michael Clarke Duncan', 1957, 'male', 'USA');




-- inserts for reviews
INSERT INTO reviews (movie_id, review, rating, date) VALUES (1, 'Great movie!', 9, '2019-01-01');
INSERT INTO reviews (movie_id, review, rating, date) VALUES (1, 'Awesome!', 10, '2019-01-02');
INSERT INTO reviews (movie_id, review, rating, date) VALUES (2, 'Pretty good!', 9, '2010-11-22');
INSERT INTO reviews (movie_id, review, rating, date) VALUES (2, 'I liked it!', 8, '2010-11-23');
INSERT INTO reviews (movie_id, review, rating, date) VALUES (3, 'Great movie!', 9, '2019-01-01');
INSERT INTO reviews (movie_id, review, rating, date) VALUES (3, 'Awesome!', 10, '2019-01-02');
INSERT INTO reviews (movie_id, review, rating, date) VALUES (4, 'Pretty good!', 9, '2010-11-22');
INSERT INTO reviews (movie_id, review, rating, date) VALUES (4, 'I liked it!', 8, '2010-11-23');
INSERT INTO reviews (movie_id, review, rating, date) VALUES (5, 'Great movie!', 9, '2019-01-01');
INSERT INTO reviews (movie_id, review, rating, date) VALUES (5, 'Awesome!', 10, '2019-01-02');

-- inserts for movieActors
INSERT INTO movieActors (movie_id, actor_id) VALUES (1, 1);
INSERT INTO movieActors (movie_id, actor_id) VALUES (1, 2);
INSERT INTO movieActors (movie_id, actor_id) VALUES (2, 3);
INSERT INTO movieActors (movie_id, actor_id) VALUES (2, 4);
INSERT INTO movieActors (movie_id, actor_id) VALUES (3, 4);
INSERT INTO movieActors (movie_id, actor_id) VALUES (3, 5);
INSERT INTO movieActors (movie_id, actor_id) VALUES (4, 6);
INSERT INTO movieActors (movie_id, actor_id) VALUES (4, 7);
INSERT INTO movieActors (movie_id, actor_id) VALUES (5, 8);
INSERT INTO movieActors (movie_id, actor_id) VALUES (5, 9);




