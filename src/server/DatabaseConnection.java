package server;

import utilities.Account;
import utilities.Bill;
import utilities.Movie;
import utilities.Rental;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {
    private final String url = "";
    private final String user = "";
    private final String password = "";
    private Connection c;

    public DatabaseConnection() {
        try {
            c = DriverManager.getConnection(url, user, password);
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            c.close();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public int accountExists(Account acc) {
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM users;");

            while(rs.next()) {
                if(rs.getString("login_field").equals(acc.getAccountName()) && rs.getString("password_field").equals(acc.getAccountPassword())) {
                    if(rs.getString("account_type").equals("0")) {
                        rs.close();
                        s.close();
                        return 0;
                    }
                    else {
                        rs.close();
                        s.close();
                        return 1;
                    }
                }
            }

            rs.close();
            s.close();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return -1;
    }

    public int createAccount(Account acc) {
        try {
            Statement s = c.createStatement();
            s.executeUpdate("INSERT INTO users (login_field,email_field,password_field,account_type) " +
                    "VALUES ('" + acc.getAccountName() + "','" + acc.getAccountEmail() + "','"
                    + acc.getAccountPassword() + "','0');");
            s.close();
            return 0;
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }

        return -1;
    }

    public ArrayList<Rental> getRentalList(Account acc) {
        ArrayList<Rental> rentalList = new ArrayList<Rental>();

        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM moviesrentals WHERE login_field='" + acc.getAccountName() + "';");

            while (rs.next()) {
                rentalList.add(new Rental(rs.getString("movie_name"), rs.getString("rental_date"),
                        rs.getString("return_date")));
            }

            rs.close();
            s.close();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return rentalList;
    }

    public ArrayList<Bill> getBillsList(Account acc) {
        ArrayList<Bill> billsList = new ArrayList<Bill>();

        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM rentalbills WHERE login_field='" + acc.getAccountName() + "';");

            while (rs.next()) {
                billsList.add(new Bill(rs.getString("movie_name"), rs.getString("return_date"),
                        rs.getString("return_date2"), rs.getString("fee")));
            }

            rs.close();
            s.close();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return billsList;
    }

    public int movieAvaible(Movie mov) {
        try {
            CallableStatement cs = c.prepareCall("{? = CALL movieavaible(?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, mov.getMovieName());
            cs.execute();

            if(cs.getInt(1) == 1) {
                cs.close();
                return 1;
            }
            else if(cs.getInt(1) == 0) {
                cs.close();
                return 0;
            }
            else {
                cs.close();
                return -1;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }

        return -1;
    }

    public int rentNewFilm(Movie mov, Account acc) {
        try {
            Statement s = c.createStatement();
            s.executeUpdate("INSERT INTO moviesrentals (login_field,movie_name,rental_date,return_date) " +
                    "VALUES ('" + acc.getAccountName() + "','" + mov.getMovieName()+ "',curdate(),curdate()+7);");
            s.close();
            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }

        return -1;
    }

    public int returnFilm(Rental rent) {
        try {
            CallableStatement cs = c.prepareCall("{? = CALL returnfilm(?,?,?)}");
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setString(2, rent.getRentalMovieName());
            cs.setDate(3, Date.valueOf(rent.getRentalDate()));
            cs.setDate(4, Date.valueOf(rent.getRentalReturnDate()));
            cs.execute();

            if(cs.getBoolean(1)) {
                cs.close();
                return 0;
            }
            else {
                cs.close();
                return -1;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }

        return -1;
    }

    public int payBill(Bill bill) {
        try {
            Statement s = c.createStatement();
            s.executeUpdate("DELETE FROM rentalbills WHERE movie_name='" +
                    bill.getBillsMovieName() + "' AND return_date='" +
                    bill.getBillsReturnDate() + "' AND return_date2='" +
                    bill.getBillsReturnDate2() + "' AND fee=" +
                    bill.getBillsFee() + ";");
            s.close();
            return 0;
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }

        return -1;
    }

    public int changeCredentials(Account acc) {
        try {
            Statement s = c.createStatement();
            s.executeUpdate("UPDATE users SET email_field='" + acc.getAccountEmail() +
                    "', password_field='" + acc.getAccountPassword() + "' WHERE login_field='" + acc.getAccountName() + "';");
            s.close();
            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public ArrayList<Movie> getMovieList() {
        ArrayList<Movie> movieList = new ArrayList<Movie>();

        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM movies;");

            while (rs.next()) {
                movieList.add(new Movie(rs.getString("movie_name"), rs.getDouble("price")));
            }

            rs.close();
            s.close();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return movieList;
    }

    public int addNewFilm(Movie mov) {
        try {
            Statement s = c.createStatement();
            s.executeUpdate("INSERT INTO movies (movie_name,price) " +
                    "VALUES ('" + mov.getMovieName() + "'," + mov.getMoviePrice() + ");");
            s.close();
            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }

        return -1;
    }

    public int modifyFilm(Movie mov) {
        try {
            Statement s = c.createStatement();
            s.executeUpdate("UPDATE movies SET movie_name='" +
                    mov.getMovieName() + "', price=" + mov.getMoviePrice() + " WHERE movie_name='" +
                    mov.getOldMovieName() + "';");
            s.close();
            return 0;
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }

        return -1;
    }

    public int deleteFilm(Movie mov) {
        try {
            Statement s = c.createStatement();
            s.executeUpdate("DELETE FROM movies WHERE movie_name='" +
                     mov.getMovieName() + "' AND price=" + mov.getMoviePrice() + ";");
            s.close();
            return 0;
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }

        return -1;
    }
}