package utilities;

import java.io.Serializable;
import java.util.ArrayList;

public class SerializationObject implements Serializable {
    private int whichAction;
    private ArrayList<Movie> moviesList = new ArrayList<Movie>();
    private ArrayList<Rental> rentalList = new ArrayList<Rental>();
    private ArrayList<Bill> billsList = new ArrayList<Bill>();
    private Account acc;
    private Movie mov;
    private Rental rent;
    private Bill bill;

    public SerializationObject(int whichAction) {
        this.whichAction = whichAction;
    }

    public SerializationObject(int whichAction, Account acc) {
        this.whichAction = whichAction;
        this.acc = acc;
    }

    public void setMovie(Movie mov) {this.mov = mov;}
    public void setRent(Rental rent) {this.rent = rent;}
    public void setBill(Bill bill) {this.bill = bill;}
    public void setMoviesList(ArrayList<Movie> moviesList) {this.moviesList = moviesList;}
    public void setRentalList(ArrayList<Rental> rentalList) {this.rentalList = rentalList;}
    public void setBillsList(ArrayList<Bill> billsList) {this.billsList = billsList;}

    public int getAction() {return whichAction;}
    public Account getAccount() {return acc;}
    public Movie getMovie() {return mov;}
    public Rental getRent() {return rent;}
    public Bill getBill() {return bill;}
    public ArrayList<Movie> getMoviesList() {return moviesList;}
    public ArrayList<Rental> getRentalList() {return rentalList;}
    public ArrayList<Bill> getBillsList() {return billsList;}
}
