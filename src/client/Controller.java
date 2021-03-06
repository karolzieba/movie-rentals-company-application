package client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilities.Account;
import utilities.SerializationObject;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private Button loginButton, changeCredentialsButton;
    @FXML private TextField loginFieldAtLogin, loginFieldAtRegister, emailFieldAtRegister, newEmailField;
    @FXML private PasswordField passwordFieldAtLogin, passwordFieldAtRegister, passwordFieldAtRegister2,
            newPasswordField;
    @FXML private Hyperlink registerHyperlink, loginHyperlink;

    private SerializationObject so;
    private Account acc;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

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

    public void registerHyperlinkEvent(){
        Stage currentStage = (Stage)registerHyperlink.getScene().getWindow();
        currentStage.setScene(Main.scene[1]);
        currentStage.setHeight(435.0);
    }

    public void loginHyperlinkEvent(){
        Stage currentStage = (Stage)loginHyperlink.getScene().getWindow();
        currentStage.setScene(Main.scene[0]);
        currentStage.setHeight(275.0);
    }

    public void loginButtonEvent() {
        Stage currentStage = (Stage)loginButton.getScene().getWindow();

        if(loginFieldAtLogin.getText().equals("") && passwordFieldAtLogin.getText().equals("")) {
            loginFieldAtLogin.setPromptText("Nie podano loginu!");
            passwordFieldAtLogin.setPromptText("Nie podano has??a!");
        }
        else if(loginFieldAtLogin.getText().equals("")) {
            loginFieldAtLogin.setPromptText("Nie podano loginu!");
        }
        else if(passwordFieldAtLogin.getText().equals("")) {
            passwordFieldAtLogin.setPromptText("Nie podano has??a!");
        }
        else {
            acc = new Account(loginFieldAtLogin.getText(), passwordFieldAtLogin.getText());
            so = new SerializationObject(1, acc);

            try {
                sendSerializedData(so);
                int result = getData();

                if(result == 0) {
                    Main.setCurrentAcc(acc);

                    Main.root[3] = FXMLLoader.load(getClass().getResource("fxml/4.fxml"));
                    Main.scene[3] = new Scene(Main.root[3], 1000, 425);
                    currentStage.setHeight(375.0);

                    currentStage.setScene(Main.scene[3]);
                }
                else if(result == 1) {
                    Main.setCurrentAcc(acc);

                    Main.root[4] = FXMLLoader.load(getClass().getResource("fxml/5.fxml"));
                    Main.scene[4] = new Scene(Main.root[4], 500, 290);
                    currentStage.setHeight(330.0);

                    currentStage.setScene(Main.scene[4]);
                }
                else {
                    System.out.println("Konto o takich danych nie istnieje!");
                }

                loginFieldAtLogin.clear();
                passwordFieldAtLogin.clear();

                loginFieldAtLogin.setPromptText("LOGIN");
                passwordFieldAtLogin.setPromptText("HAS??O");
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void registerButtonEvent() throws IOException {
        if(loginFieldAtRegister.getText().equals("")) {
            loginFieldAtRegister.setPromptText("Nie podano loginu!");
        }
        else if(loginFieldAtRegister.getText().length() > 10) {
            loginFieldAtRegister.clear();

            loginFieldAtRegister.setPromptText("Login ma wi??cej ni?? 10 znak??w!");
        }
        else if(emailFieldAtRegister.getText().equals("")) {
            emailFieldAtRegister.setPromptText("Nie podano adresu email!");
        }
        else if(emailFieldAtRegister.getText().length() > 25)  {
            emailFieldAtRegister.clear();

            emailFieldAtRegister.setPromptText("E-mail ma wi??cej ni?? 25 znak??w!");
        }
        else if(!emailFieldAtRegister.getText().contains("@")) {
            emailFieldAtRegister.clear();

            emailFieldAtRegister.setPromptText("E-mail nie zawiera @!");
        }
        else if(passwordFieldAtRegister.getText().equals("")) {
            passwordFieldAtRegister.setPromptText("Nie podano has??a!");
        }
        else if(passwordFieldAtRegister2.getText().equals("")){
            passwordFieldAtRegister2.setPromptText("Nie podano has??a!");
        }
        else if(passwordFieldAtRegister.getText().length() > 10) {
            passwordFieldAtRegister.clear();

            passwordFieldAtRegister.setPromptText("Has??o ma wi??cej ni?? 10 znak??w!");
        }
        else if(!passwordFieldAtRegister.getText().equals(passwordFieldAtRegister2.getText())) {
            passwordFieldAtRegister.clear();
            passwordFieldAtRegister2.clear();

            passwordFieldAtRegister.setPromptText("Has??a nie s?? takie same!");
            passwordFieldAtRegister2.setPromptText("Has??a nie s?? takie same!");
        }
        else {
            acc = new Account(loginFieldAtRegister.getText(),
                    emailFieldAtRegister.getText(), passwordFieldAtRegister.getText());
            so = new SerializationObject(2, acc);
            sendSerializedData(so);

            int result = getData();
            if(result == 0) {
                loginFieldAtRegister.clear();
                emailFieldAtRegister.clear();
                passwordFieldAtRegister.clear();
                passwordFieldAtRegister2.clear();

                loginFieldAtRegister.setPromptText("LOGIN");
                emailFieldAtRegister.setPromptText("E-MAIL");
                passwordFieldAtRegister.setPromptText("HAS??O");
                passwordFieldAtRegister2.setPromptText("POWT??RZ HAS??O");

                System.out.println("Uda??o si?? zarejestrowa?? podane konto!");
            }
            else {
                System.out.println("Nie uda??o si?? zarejestrowa?? podanego konta!");
            }
        }
    }

    public void changeCredentialsButtonEvent() throws IOException {
        Stage currentStage = (Stage)changeCredentialsButton.getScene().getWindow();

        if(newEmailField.getText().equals("") && newPasswordField.getText().equals("")) {
            newEmailField.setPromptText("Nie podano nowego emaila i has??a!");
            newPasswordField.setPromptText("Nie podano nowego emaila i has??a!");
        }
        else if(newEmailField.getText().equals("")) {
            newEmailField.clear();

            newEmailField.setPromptText("Nie podano nowego emaila!");
        }
        else if(newPasswordField.getText().equals("")) {
            newPasswordField.clear();

            newPasswordField.setPromptText("Nie podano nowego has??a!");
        }
        else if(newEmailField.getText().length() > 25) {
            newEmailField.clear();

            newEmailField.setPromptText("E-mail ma wi??cej ni?? 25 znak??w!");
        }
        else if(!newEmailField.getText().contains("@")) {
            newEmailField.clear();

            newEmailField.setPromptText("E-mail nie zawiera @!");
        }
        else if(newPasswordField.getText().length() > 10) {
            newPasswordField.clear();

            newPasswordField.setPromptText("Has??o ma wi??cej ni?? 10 znak??w!");
        }
        else {
            acc = new Account(Main.getCurrentAcc().getAccountName(),
                    newEmailField.getText(), newPasswordField.getText());
            so = new SerializationObject(9, acc);
            sendSerializedData(so);

            int result = getData();
            if(result == 0){
                Main.setCurrentAcc(acc);

                System.out.println("Uda??o si?? zmieni?? email i has??o!");
            }
            else {
                System.out.println("Nie uda??o si?? zmieni?? emaila i has??a!");
            }

            newEmailField.clear();
            newPasswordField.clear();

            currentStage.close();
        }
    }
}
