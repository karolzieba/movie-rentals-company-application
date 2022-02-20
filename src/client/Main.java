package client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utilities.Account;
import java.io.IOException;
import java.net.Socket;

public class Main extends Application {
    public static Parent[] root = new Parent[5];
    public static Scene[] scene = new Scene[5];
    private static Socket sock;
    private static final String host = "localhost";
    private static final int PORT = 3307;
    private static Account currentAcc;

    @Override
    public void start(Stage primaryStage) throws Exception{
        root[0] = FXMLLoader.load(getClass().getResource("fxml/1.fxml"));
        root[1] = FXMLLoader.load(getClass().getResource("fxml/2.fxml"));
        root[2] = FXMLLoader.load(getClass().getResource("fxml/3.fxml"));
        scene[0] = new Scene(root[0], 500, 250);
        scene[1] = new Scene(root[1], 500, 400);
        scene[2] = new Scene(root[2], 500, 200);

        primaryStage.setTitle("Wypożyczalnia filmów");
        primaryStage.setScene(scene[0]);
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                try {
                    sock.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                primaryStage.close();
            }
        });
    }

    public static void main(String[] args) {
        try {
            sock = new Socket(host, PORT);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        launch(args);
    }

    public static void setCurrentAcc(Account acc) {currentAcc = acc;}

    public static Account getCurrentAcc() {return currentAcc;}
    public static Socket getSocket() {return sock;}
}
