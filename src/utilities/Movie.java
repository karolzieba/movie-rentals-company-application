package utilities;

import java.io.Serializable;

public class Movie implements Serializable {
    private String movieName;
    private String oldMovieName;
    private double moviePrice;

    public Movie(String movieName) {
        this.movieName = movieName;
    }

    public Movie(String movieName, double moviePrice) {
        this.movieName = movieName;
        this.moviePrice = moviePrice;
    }

    public Movie(String movieName, String oldMovieName, double moviePrice) {
        this.movieName = movieName;
        this.oldMovieName = oldMovieName;
        this.moviePrice = moviePrice;
    }

    public String getMovieName() {return movieName;}
    public String getOldMovieName() {return oldMovieName;}
    public double getMoviePrice() {return moviePrice;}
}
