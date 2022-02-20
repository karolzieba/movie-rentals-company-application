package server;

import utilities.SerializationObject;
import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable {
    private final Socket sock;
    private final int num;

    private DatabaseConnection dbc;

    private SerializationObject so;

    public ServerThread(Socket sock, int num) {
        this.sock = sock;
        this.num = num;
        dbc = new DatabaseConnection();

        System.out.println("Podłączono klienta o numerze " + num + ".");
    }

    public void close() {
        try {
            dbc.closeConnection();
            sock.close();
            System.out.println("Odłączono klienta o numerze " + num + ".");
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void sendData(int data) throws IOException {
        OutputStream os = sock.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(data);
    }

    public void sendSerializedData(SerializationObject so) throws IOException {
        OutputStream os = sock.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(so);
    }

    public SerializationObject getSerializedData() throws IOException, ClassNotFoundException {
        InputStream is = sock.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        return (SerializationObject)ois.readObject();
    }

    @Override
    public void run() {
        while(true){
            try {
                so = getSerializedData();

                switch(so.getAction()) {
                    case 1:
                        sendData(dbc.accountExists(so.getAccount()));
                        break;
                    case 2:
                        sendData(dbc.createAccount(so.getAccount()));
                        break;
                    case 3:{
                        so.setRentalList(dbc.getRentalList(so.getAccount()));
                        sendSerializedData(so);
                        break;
                    }
                    case 4:{
                        so.setBillsList(dbc.getBillsList(so.getAccount()));
                        sendSerializedData(so);
                        break;
                    }
                    case 5:
                        sendData(dbc.movieAvaible(so.getMovie()));
                        break;
                    case 6:
                        sendData(dbc.rentNewFilm(so.getMovie(), so.getAccount()));
                        break;
                    case 7:
                        sendData(dbc.returnFilm(so.getRent()));
                        break;
                    case 8:
                        sendData(dbc.payBill(so.getBill()));
                        break;
                    case 9:
                        sendData(dbc.changeCredentials(so.getAccount()));
                        break;
                    case 10:{
                        so.setMoviesList(dbc.getMovieList());
                        sendSerializedData(so);
                        break;
                    }
                    case 11:
                        sendData(dbc.addNewFilm(so.getMovie()));
                        break;
                    case 12:
                        sendData(dbc.modifyFilm(so.getMovie()));
                        break;
                    case 13:
                        sendData(dbc.deleteFilm(so.getMovie()));
                        break;
                    default:
                        break;
                }
            }
            catch(IOException | ClassNotFoundException e) {
                close();
                return;
            }
        }
    }
}
