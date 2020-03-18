import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        Socket connection = new Socket("localhost", 7000);

        InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(readConnection);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

        String msg = reader.readLine();

        while(msg != null){
            System.out.println(msg);


            System.out.println("Enter which operation to do");
            String operation = in.nextLine();

            System.out.println("enter number 1");
            String no1 = in.nextLine();

            System.out.println("enter number 2");
            String no2 = in.nextLine();

            //Could also combine to take one input i.e "add 2 9"
            writer.println(operation + " " + no1 + " " + no2);

            msg = reader.readLine();

        }


    }
}