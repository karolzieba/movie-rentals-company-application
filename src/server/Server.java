package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket servsock = new ServerSocket(3307);
        System.out.println("Serwer włączony....");

        int num = 0;
        try {
            while (true) {
                Socket sock = servsock.accept();

                num++;
                new Thread(new ServerThread(sock, num)).start();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
