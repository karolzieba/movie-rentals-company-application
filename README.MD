[PL]
Projekt został wykonany w czerwcu 2021 jako projekt uczelniany w ramach przedmiotu związanego z programowaniem obiektowym w języku Java.
Jest to prosta aplikacja okienkowa stworzona dla wypożyczalni filmów. Zawiera interfejsy użytkownika dla pracownika i klienta. Każdy typ konta posiada własne funkcje. Program został wykonany w architekturze klient - serwer (bazuje na Socketach) i używa bazy danych MySQL (JDBC). Do stworzenia GUI, wykorzystałem technologię JavaFX.

Jak uruchomić projekt:
1. Należy najpierw zainstalować JavaFX.
2. Skonfiguruj połączenie z twoją bazą danych MySQL w pliku "src/server/DatabaseConnection.java" (pola "url", "user" oraz "password").
3. Uruchom skrypty SQL z folderu "sql_scripts" na twoim serwerze DB (Znajdują się tutaj tabele, wyzwalacz i funkcje).
4. Skompiluj obie aplikacje (Klient i Serwer).
5. Uruchom najpierw aplikację serwera (plik "Server.class"), a później również aplikację kliencką (plik "Main.class").

[ENG]
The project was done at June 2021 as a university project for subject associated with object programming in Java language.
It is a simple desktop application made for movie rentals company. It contains user interfaces for employee and client. Every type of account has its own features. This program was made in client - server architecture (based on Sockets) and uses MySQL database (JDBC). To create the GUI, I used JavaFX technology.

How to run the project:
1. You have to install JavaFX first.
2. Configure connection with your MySQL database in "src/server/DatabaseConnection.java" file ("url", "user" and "password" fields).
3. Run SQL scripts from "sql_scripts" folder at your DB server (Tables, trigger and functions are here).
4. Compile both applications (Client and Server).
5. Run server application first ("Server.class" file) and then client application ("Main.class" file) too.