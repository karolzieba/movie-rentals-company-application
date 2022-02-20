package utilities;

import java.io.Serializable;

public class Rental implements Serializable {
    private String rentalMovieName;
    private String rentalDate;
    private String rentalReturnDate;

    public Rental(String rentalMovieName, String rentalDate, String rentalReturnDate) {
        this.rentalMovieName = rentalMovieName;
        this.rentalDate = rentalDate;
        this.rentalReturnDate = rentalReturnDate;
    }

    public String getRentalMovieName() {return rentalMovieName;}
    public String getRentalDate() {return rentalDate;}
    public String getRentalReturnDate() {return rentalReturnDate;}
}
