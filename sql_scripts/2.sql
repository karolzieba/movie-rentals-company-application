-- Usuwanie wyzwalacza.
DROP TRIGGER insert_bill;

-- Tworzenie wyzwalacza.
-- Wyzwalacz ten działa w taki sposób, że po zwrocie filmu (tak naprawdę usunięciu rekordu z tabeli wypożyczeń), 
-- tworzy nowy rekord w tabeli rachunków powiązany z wypożyczeniem (wylicza tam między innymi ostateczną kwotę do zapłacenia).
delimiter //

CREATE TRIGGER insert_bill
AFTER DELETE ON moviesrentals FOR EACH ROW
BEGIN
	 DECLARE pr, f DOUBLE;
    
     SELECT m.price INTO pr FROM movies m WHERE m.movie_name = OLD.movie_name;
    
	 IF (curdate() - OLD.return_date) > 0 THEN
		SET f = pr + ((curdate() - OLD.return_date) * 1.0);
	 ELSE
		SET f = pr;
	 END IF;

	 INSERT INTO rentalbills (login_field, movie_name, return_date, return_date2, fee) VALUES
     (OLD.login_field, OLD.movie_name, OLD.return_date, curdate(), f);
END//
