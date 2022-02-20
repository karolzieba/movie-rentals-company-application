package utilities;

import java.io.Serializable;

public class Bill implements Serializable {
    private String billsMovieName;
    private String billsReturnDate;
    private String billsReturnDate2;
    private String billsFee;

    public Bill(String billsMovieName, String billsReturnDate, String billsReturnDate2, String billsFee) {
        this.billsMovieName = billsMovieName;
        this.billsReturnDate = billsReturnDate;
        this.billsReturnDate2 = billsReturnDate2;
        this.billsFee = billsFee;
    }

    public String getBillsMovieName() {return billsMovieName;}
    public String getBillsReturnDate() {return billsReturnDate;}
    public String getBillsReturnDate2() {return billsReturnDate2;}
    public String getBillsFee() {return billsFee;}
}
