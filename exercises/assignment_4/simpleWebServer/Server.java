
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[]args) throws IOException {
        
        final int port = 80;
        ServerSocket ss = new ServerSocket(port);
        System.out.println("Server running on port " + port);
        Socket connection = ss.accept();

        InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(readConnection);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);
        String msg = reader.readLine();

        ArrayList<String> header = new ArrayList<>();
        while(!msg.equals("")){
            header.add(msg);
            System.out.println(msg);
            msg = reader.readLine();
        }

        writer.println("HTTP/1.0 200 OK\r");
        writer.println("Content-Type: text/html; charset=utf-8\r");
        writer.println("Server: SimpleWebServer\r");
        writer.println("\r\n");
        writer.println("\r\n");

        writer.println("<HTML>\r\n<BODY>\r\n");
        writer.println("<h1> Hilsen. Du har koblet deg opp til min enkle web-tjener </h1>\r\n");
        writer.println("Header fra klient er: \r\n");
        writer.println("<ul>\r");
        for(String line : header){
            writer.println("<li>" + line + "</li>\r");
        }

        writer.println("</ul>\r");
        writer.println("</BODY>\r\n</HTML>\r");
        writer.println("\r\n");

        connection.close();
    }


}