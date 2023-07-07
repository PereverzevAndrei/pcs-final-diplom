import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//import static sun.management.jmxremote.ConnectorBootstrap.PropertyNames.HOST;
//import static sun.management.jmxremote.ConnectorBootstrap.PropertyNames.PORT;

public class Client {

    public static void main(String[] args) {
        String keyword = "или";
        try (Socket socket = new Socket("localHost", 8989);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            writer.println(keyword);
            System.out.println(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
