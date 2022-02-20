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
import utilities.Movie;
import utilities.SerializationObject;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML private Button filmAddButton, filmModifyButton, filmDeleteButton;
    @FXML private TextField filmPriceField, filmNameField;
    @FXML private Hyperlink logoutHyperlinkAdmin;
    @FXML private Text loginAdminText = new Text();

    @FXML TableView<Movie> movieTable = new TableView<Movie>();
    @FXML TableColumn<Movie, String> movieName = new TableColumn<Movie, String>();
    @FXML TableColumn<Movie, String> moviePrice = new TableColumn<Movie, String>();

    private SerializationObject so;
    private Movie mov;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movieName.setCellValueFactory(new PropertyValueFactory<Movie, String>("movieName"));
        moviePrice.setCellValueFactory(new PropertyValueFactory<Movie, String>("moviePrice"));

        try {
            fetchMovieTable();
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        movieTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown() && event.getClickCount() == 1) {
                    mov = movieTable.getSelectionModel().getSelectedItem();

                    filmModifyButton.setDisable(false);
                    filmDeleteButton.setDisable(false);
                    filmNameField.setVisible(false);
                    filmPriceField.setVisible(false);

                    Stage currentStage = (Stage)movieTable.getScene().getWindow();
                    currentStage.setHeight(330.0);
                }
            }
        });

        loginAdminText.setText("Administrator: " + Main.getCurrentAcc().getAccountName());
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

    public void fetchMovieTable() throws IOException, ClassNotFoundException {
        so = new SerializationObject(10);
        sendSerializedData(so);
        so = getSerializedData();

        ObservableList<Movie> movieList = FXCollections.observableArrayList();
        movieList.addAll(so.getMoviesList());
        movieTable.setItems(movieList);
    }

    public void logoutHyperlinkAdminEvent() {
        Stage currentStage = (Stage)logoutHyperlinkAdmin.getScene().getWindow();

        filmNameField.setVisible(false);
        filmPriceField.setVisible(false);
        currentStage.setHeight(275.0);

        currentStage.setScene(Main.scene[0]);
    }

    public void addNewFilmEvent() throws IOException, ClassNotFoundException {
        Stage currentStage = (Stage)filmAddButton.getScene().getWindow();

        if(filmNameField.isVisible() == false) {
            filmModifyButton.setDisable(true);
            filmDeleteButton.setDisable(true);

            filmNameField.setVisible(true);
            filmPriceField.setVisible(true);
            currentStage.setHeight(440.0);
        }
        else {
            if(filmNameField.getText().equals("") && filmPriceField.getText().equals("")) {
                filmNameField.clear();
                filmPriceField.clear();

                filmNameField.setPromptText("Nie podano tytułu filmu!");
                filmPriceField.setPromptText("Nie podano ceny!");
            }
            else if(filmNameField.getText().length() > 30) {
                filmNameField.clear();

                filmNameField.setPromptText("Nazwa filmu ma więcej niż 30 znaków!");
            }
            else if(filmPriceField.getText().equals("")) {
                filmPriceField.clear();

                filmPriceField.setPromptText("Nie podano ceny!");
            }
            else if(filmNameField.getText().equals("")) {
                filmNameField.clear();

                filmNameField.setPromptText("Nie podano tytułu filmu!");
            }
            else {
                mov = new Movie(filmNameField.getText(), Double.parseDouble(filmPriceField.getText()));

                so = new SerializationObject(11);
                so.setMovie(mov);
                sendSerializedData(so);

                int result = getData();
                if(result == 0) {
                    fetchMovieTable();

                    filmNameField.clear();
                    filmPriceField.clear();

                    filmNameField.setVisible(false);
                    filmPriceField.setVisible(false);
                    currentStage.setHeight(330.0);

                    System.out.println("Udało się dodać nowy film do bazy!");
                }
                else {
                    System.out.println("Nie udało się dodać nowego filmu do bazy!");
                }
            }
        }
    }

    public void modifyFilmEvent() throws IOException, ClassNotFoundException {
        Stage currentStage = (Stage)filmModifyButton.getScene().getWindow();

        if(filmNameField.isVisible() == false) {
            filmNameField.setVisible(true);
            filmPriceField.setVisible(true);
            currentStage.setHeight(440.0);
        }
        else {
            if(filmNameField.getText().equals("") && filmPriceField.getText().equals("")) {
                filmNameField.setPromptText("Nie podano tytułu filmu!");
                filmPriceField.setPromptText("Nie podano ceny!");
            }
            else if(filmNameField.getText().length() > 30) {
                filmNameField.clear();

                filmNameField.setPromptText("Nazwa filmu ma więcej niż 30 znaków!");
            }
            else if(filmNameField.getText().equals("")) {
                filmNameField.setPromptText("Nie podano tytułu filmu!");
            }
            else if(filmPriceField.getText().equals("")) {
                filmPriceField.setPromptText("Nie podano ceny filmu!");
            }
            else {
                mov = new Movie(filmNameField.getText(), mov.getMovieName(), Double.parseDouble(filmPriceField.getText()));
                so = new SerializationObject(12);
                so.setMovie(mov);
                sendSerializedData(so);

                int result = getData();
                if(result == 0) {
                    fetchMovieTable();

                    filmNameField.clear();
                    filmPriceField.clear();

                    filmModifyButton.setDisable(true);
                    filmDeleteButton.setDisable(true);

                    filmNameField.setVisible(false);
                    filmPriceField.setVisible(false);
                    currentStage.setHeight(330.0);

                    System.out.println("Udało się zmodyfikować film w bazie!");
                }
                else {
                    filmNameField.clear();
                    filmPriceField.clear();

                    filmNameField.setPromptText("Nazwa filmu");
                    filmPriceField.setPromptText("Cena");

                    System.out.println("Nie udało się zmodyfikować filmu w bazie!");
                }
            }
        }
    }

    public void deleteFilmEvent() throws IOException, ClassNotFoundException {
        so = new SerializationObject(13);
        so.setMovie(mov);
        sendSerializedData(so);

        int result = getData();
        if(result == 0) {
            fetchMovieTable();

            filmDeleteButton.setDisable(true);
            filmModifyButton.setDisable(true);

            System.out.println("Udało się usunąć film z bazy!");
        }
        else {
            System.out.println("Nie udało się usunąć filmu z bazy!");
        }
    }

    public void changeCredentialsHyperlinkEvent() {
        Stage newStage = new Stage();

        newStage.setTitle("Wypożyczalnia filmów - Zmiana danych");
        newStage.setScene(Main.scene[2]);
        newStage.setResizable(false);
        newStage.show();
    }
}
