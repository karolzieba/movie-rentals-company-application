-- Usuwanie tabel.
DROP TABLE users;
DROP TABLE movies;
DROP TABLE moviesrentals;
DROP TABLE rentalbills;

-- Tworzenie tabel.
-- Tabela users przechowuje dane o użytkownikach.
CREATE TABLE users (
	login_field VARCHAR(10) NOT NULL PRIMARY KEY, 
    email_field VARCHAR(25) NOT NULL UNIQUE,
    password_field VARCHAR(10) NOT NULL,
    account_type VARCHAR(1) NOT NULL
);

-- Tabela movies przechowuje dane o filmach.
CREATE TABLE movies (
	movie_name VARCHAR(30) NOT NULL PRIMARY KEY,
    price DOUBLE NOT NULL CHECK (price > 0)
);

-- Tabela moviesrentals przechowuje dane o aktualnych wypożyczeniach filmów przez klientów.
CREATE TABLE moviesrentals (
	login_field VARCHAR(10) NOT NULL,
    movie_name VARCHAR(30) NOT NULL UNIQUE,
    rental_date DATE NOT NULL,
    return_date DATE NOT NULL,
    PRIMARY KEY (login_field, movie_name),
    FOREIGN KEY (login_field) REFERENCES users(login_field),
    FOREIGN KEY (movie_name) REFERENCES movies(movie_name)
);

-- Tabela rentalbills przechowuje dane o aktualnych rachunkach do zapłacenia przez klientów.
CREATE TABLE rentalbills (
	login_field VARCHAR(10) NOT NULL REFERENCES users(login_field),
    movie_name VARCHAR(30) NOT NULL REFERENCES movies(movie_name),
    return_date DATE NOT NULL,
    return_date2 DATE NOT NULL,
    fee DOUBLE NOT NULL,
    PRIMARY KEY (login_field, movie_name, return_date),
    FOREIGN KEY (login_field) REFERENCES users(login_field),
    FOREIGN KEY (movie_name) REFERENCES movies(movie_name)
);

-- Polecenie tworzy konto administratora.
INSERT INTO users (login_field, email_field, password_field, account_type) VALUES ('admin', 'admin@admin.pl', 'ad_pass', 1);

-- Wyświetlanie tabel.
SELECT * FROM users;
SELECT * FROM movies;
SELECT * FROM moviesrentals;
SELECT * FROM rentalbills;
