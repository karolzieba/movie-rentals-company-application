package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utilities.Bill;
import utilities.Movie;
import utilities.Rental;
import utilities.SerializationObject;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    @FXML private Button rentNewFilmButton, returnFilmButton, payBillButton,
            movieYesButton, movieNoButton;
    @FXML private Text loginUserText;
    @FXML private Hyperlink logoutHyperlink;
    @FXML private TextField movieTextField;

    @FXML private TableView<Rental> rentalsTable = new TableView<Rental>();
    @FXML private TableColumn<Rental, String> rentalMovieName = new TableColumn<Rental, String>();
    @FXML private TableColumn<Rental, String> rentalDate = new TableColumn<Rental, String>();
    @FXML private TableColumn<Rental, String> rentalReturnDate = new TableColumn<Rental, String>();

    @FXML private TableView<Bill> billsTable = new TableView<Bill>();
    @FXML private TableColumn<Bill, String> billsMovieName = new TableColumn<Bill, String>();
    @FXML private TableColumn<Bill, String> billsReturnDate = new TableColumn<Bill, String>();
    @FXML private TableColumn<Bill, String> billsReturnDate2 = new TableColumn<Bill, String>();
    @FXML private TableColumn<Bill, String> billsFee = new TableColumn<Bill, String>();

    private SerializationObject so;
    private Movie mov;
    private Rental rent;
    private Bill bill;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rentalMovieName.setCellValueFactory(new PropertyValueFactory<Rental, String>("rentalMovieName"));
        rentalDate.setCellValueFactory(new PropertyValueFactory<Rental, String>("rentalDate"));
        rentalReturnDate.setCellValueFactory(new PropertyValueFactory<Rental, String>("rentalReturnDate"));

        billsMovieName.setCellValueFactory(new PropertyValueFactory<Bill, String>("billsMovieName"));
        billsReturnDate.setCellValueFactory(new PropertyValueFactory<Bill, String>("billsReturnDate"));
        billsReturnDate2.setCellValueFactory(new PropertyValueFactory<Bill, String>("billsReturnDate2"));
        billsFee.setCellValueFactory(new PropertyValueFactory<Bill, String>("billsFee"));

        try {
            fetchRentalTable();
            fetchBillsTable();
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        rentalsTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown() && event.getClickCount() == 1) {
                    rent = rentalsTable.getSelectionModel().getSelectedItem();

                    payBillButton.setDisable(true);
                    returnFilmButton.setDisable(false);
                }
            }
        });

        billsTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown() && event.getClickCount() == 1) {
                    bill = billsTable.getSelectionModel().getSelectedItem();

                    returnFilmButton.setDisable(true);
                    payBillButton.setDisable(false);
                }
            }
        });

        loginUserText.setText("Użytkownik: " + Main.getCurrentAcc().getAccountName());
    }

    public int getData() throws IOException {
        InputStream is = Main.getSocket().getInputStream();
        DataInputStream dis = new DataInputStream(is);
        return dis.readInt();
    }

    public void sendSerializedData(SerializationObject so) throws IOException {
        OutputStream os = Main.getSocket().getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(so);
    }

    public SerializationObject getSerializedData() throws IOException, ClassNotFoundException {
        InputStream is = Main.getSocket().getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        return (SerializationObject)ois.readObject();
    }

    public ArrayList<Rental> getDataForRentalTable() throws IOException, ClassNotFoundException {
        so = new SerializationObject(3, Main.getCurrentAcc());
        sendSerializedData(so);
        so = getSerializedData();

        return so.getRentalList();
    }

    public void fetchRentalTable() throws IOException, ClassNotFoundException {
        ObservableList<Rental> rentalList = FXCollections.observableArrayList();
        rentalList.addAll(getDataForRentalTable());
        rentalsTable.setItems(rentalList);
    }

    public ArrayList<Bill> getDataForBillsTable() throws IOException, ClassNotFoundException {
        so = new SerializationObject(4, Main.getCurrentAcc());
        sendSerializedData(so);
        so = getSerializedData();

        return so.getBillsList();
    }

    public void fetchBillsTable() throws IOException, ClassNotFoundException {
        ObservableList<Bill> billsList = FXCollections.observableArrayList();
        billsList.addAll(getDataForBillsTable());
        billsTable.setItems(billsList);
    }

    public void logoutHyperlinkEvent() {
        Stage currentStage = (Stage)logoutHyperlink.getScene().getWindow();

        currentStage.setHeight(275.0);

        currentStage.setScene(Main.scene[0]);
    }

    public void rentNewFilmButtonEvent() throws IOException {
        Stage currentStage = (Stage)rentNewFilmButton.getScene().getWindow();

        if(movieTextField.isVisible() == false) {
            movieTextField.setVisible(true);
            currentStage.setHeight(445.0);
        }
        else if(movieTextField.isVisible() == true && movieTextField.getText().equals("")){
            movieTextField.clear();
            movieTextField.setPromptText("Nazwa filmu");
            movieTextField.setVisible(false);
            movieYesButton.setVisible(false);
            movieNoButton.setVisible(false);
            currentStage.setHeight(375.0);
        }
        else {
            if(movieTextField.getText().length() > 30) {
                movieTextField.clear();

                movieTextField.setPromptText("Nazwa filmu ma więcej niż 30 znaków!");
            }
            else if(movieTextField.getText().equals("")) {
                movieTextField.setPromptText("Nie podano tytułu filmu!");
            }
            else {
                so = new SerializationObject(5);
                mov = new Movie(movieTextField.getText());
                so.setMovie(mov);
                sendSerializedData(so);

                int result = getData();
                if(result == 1) {
                    movieYesButton.setVisible(true);
                    movieNoButton.setVisible(true);

                    movieTextField.clear();
                    movieTextField.setPromptText("Znalaziono podany film. Czy wypożyczyć?");
                }
                else if(result == 0) {
                    movieTextField.clear();

                    movieTextField.setPromptText("Film o tym tytule jest już wypożyczony.");
                }
                else {
                    movieTextField.clear();

                    movieTextField.setPromptText("Film o tym tytule nie jest dostępny.");
                }
            }
        }
    }

    public void returnFilmButtonEvent() throws IOException, ClassNotFoundException {
        so = new SerializationObject(7);
        so.setRent(rent);
        sendSerializedData(so);

        int result = getData();
        if(result == 0) {
            fetchRentalTable();
            fetchBillsTable();

            returnFilmButton.setDisable(true);

            System.out.println("Udało się zwrócić film!");
        }
        else {
            System.out.println("Nie udało się zwrócić filmu!");
        }
    }

    public void payBillButtonEvent() throws IOException, ClassNotFoundException {
        so = new SerializationObject(8);
        so.setBill(bill);
        sendSerializedData(so);

        int result = getData();
        if(result == 0) {
            fetchBillsTable();
            payBillButton.setDisable(true);

            System.out.println("Udało się opłacić rachunek za film!");
        }
        else {
            System.out.println("Nie udało się opłacić rachunku za film!");
        }
    }

    public void movieYesButtonEvent() throws IOException, ClassNotFoundException {
        Stage currentStage = (Stage)movieNoButton.getScene().getWindow();

        so = new SerializationObject(6, Main.getCurrentAcc());
        so.setMovie(mov);
        sendSerializedData(so);

        int result = getData();
        if(result == 0) {
            fetchRentalTable();

            movieTextField.clear();
            movieTextField.setPromptText("Nazwa filmu");
            movieTextField.setVisible(false);
            movieYesButton.setVisible(false);
            movieNoButton.setVisible(false);
            currentStage.setHeight(375.0);

            System.out.println("Udało się wypożyczyć film!");
        }
        else {
            movieTextField.clear();
            movieTextField.setPromptText("Nazwa filmu");
            movieYesButton.setVisible(false);
            movieNoButton.setVisible(false);

            System.out.println("Nie udało się wypożyczyć filmu!");
        }
    }

    public void movieNoButtonEvent() {
        Stage currentStage = (Stage)movieNoButton.getScene().getWindow();

        movieTextField.clear();
        movieTextField.setPromptText("Nazwa filmu");
        movieTextField.setVisible(false);
        movieYesButton.setVisible(false);
        movieNoButton.setVisible(false);
        currentStage.setHeight(375.0);
    }

    public void changeCredentialsHyperlinkEvent() {
        Stage newStage = new Stage();

        newStage.setTitle("Wypożyczalnia filmów - Zmiana danych");
        newStage.setScene(Main.scene[2]);
        newStage.setResizable(false);
        newStage.show();
    }
}
