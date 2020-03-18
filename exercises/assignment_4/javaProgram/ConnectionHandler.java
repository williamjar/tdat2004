import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler extends Thread{
    private Socket connection = null;

    public ConnectionHandler(Socket connection){
        this.connection = connection;
    }

    public void run(){
        try {
            InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(readConnection);
            PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

            writer.println("You are connected to the socket server");

            String msg = reader.readLine();

            while (msg != null) {
                String[] result = msg.split(" ");
                MathServices.Operations op = null;
                try {
                    op = MathServices.Operations.valueOf(result[0].toUpperCase());
                } catch (IllegalArgumentException e) {
                    writer.println(e);
                }


                switch (op) {
                    case ADD:
                        System.out.println("add");
                        try {
                            int no1 = Integer.parseInt(result[1]);
                            int no2 = Integer.parseInt(result[2]);
                            int resultOfOperation = MathServices.addNumbers(no1, no2);
                            writer.println(resultOfOperation);
                        } catch (NumberFormatException e) {
                            writer.println(e);
                        }
                        break;

                    case SUBTRACT:
                        System.out.println("subtract");
                        try {
                            int no1 = Integer.parseInt(result[1]);
                            int no2 = Integer.parseInt(result[2]);
                            int resultOfOperation = MathServices.subtractNumbers(no1, no2);
                            writer.println(resultOfOperation);
                        } catch (NumberFormatException e) {
                            writer.println(e);
                        }
                        break;

                }
                msg = reader.readLine();
            }

            connection.close();
        } catch (IOException e){
            System.out.println(e);
        }
    }
}
