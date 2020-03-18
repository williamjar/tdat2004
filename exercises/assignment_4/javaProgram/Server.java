import java.io.*;
import java.net.*;
import java.net.ServerSocket;
import java.util.ArrayList;


public class Server{



    public static void main(String[]args) throws IOException {

        final int PORT = 7000;
        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("Server is running on " + PORT);
        ArrayList<ConnectionHandler> connections = new ArrayList<>();
        Socket connection = null;

        while (true) {
            connection = ss.accept();
            ConnectionHandler handler = new ConnectionHandler(connection);
            handler.start();
            connections.add(handler);
        }


    }

}