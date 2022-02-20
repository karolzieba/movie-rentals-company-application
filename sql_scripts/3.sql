-- Usuwanie funkcji.
DROP FUNCTION returnfilm;
DROP FUNCTION movieavaible;

-- Tworzenie funkcji.
-- 1. Funkcja ta sprawdza czy dzisiejsza data jest przynajmniej następnym dniem daty wypożyczenia filmu,
-- pozwala to zapobiec zjawisku, które polega na wypożyczaniu filmu i natychmiastowym zwracaniu go po kilka razy na dzień.
-- Zwraca wartość true lub false, w zależności od tego czy został spełniony powyższy warunek.
delimiter //

SET GLOBAL log_bin_trust_function_creators = 1;
CREATE FUNCTION returnfilm(mn VARCHAR(30), rd DATE, rd2 DATE)
	RETURNS BOOL
    DETERMINISTIC
BEGIN
	DECLARE ret BOOL;

	IF (curdate() >= (rd + 1)) THEN
		DELETE FROM moviesrentals WHERE movie_name=mn AND rental_date=rd AND return_date=rd2;
        SET ret = TRUE;
	ELSE
		SET ret = FALSE;
	END IF;
    
    RETURN ret;
END//

-- 2. Funkcja sprawdza czy film który chce wypożyczyć klient jest dostępny. Najpierw zostaje sprawdzone czy film istnieje
-- w bazie (może być maksymalnie jeden w bazie o takiej nazwie), a jeśli jest, to sprawdza czy został już wypożyczony
-- (może istnieć maksymalnie jedno wypożyczenie danego filmu w danym momencie).
-- W zależności od sytuacji zwraca wartości: 
-- -1, jeśli film nie istnieje w bazie,
-- 0, jeśli film istnieje w bazie, ale jest wypożyczony,
-- 1, jeśli film istnieje w bazie, i jest dostępny do wypożyczenia;
delimiter //

SET GLOBAL log_bin_trust_function_creators = 1;
CREATE FUNCTION movieavaible(mov VARCHAR(30))
	RETURNS INT
	DETERMINISTIC
BEGIN
	DECLARE ret, exist, rented INT;

	SELECT COUNT(movie_name) INTO exist FROM movies WHERE movie_name=mov;
    
    IF exist = 1 THEN
		SELECT COUNT(movie_name) INTO rented FROM moviesrentals WHERE movie_name=mov;
        IF rented = 1 THEN
			SET ret = 0;
		ELSE
			SET ret = 1;
		END IF;
	ELSE
		SET ret = -1;
	END IF;
    
    RETURN ret;
END//