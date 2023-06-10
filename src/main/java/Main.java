import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import static java.lang.System.out;
import static java.lang.System.setOut;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs/SoftSkills.pdf"));
//        BooleanSearchEngine engine = new BooleanSearchEngine(new File("Продвижение игр.pdf"));
        out.println(engine.search("а"));

//        out.println(searchResult);
  //      System.out.println(engine.search("бизнес"));

//        try (ServerSocket server = new ServerSocket(8989)) {
//            out.println("Сервер запущен и ждет команды");
//            while (true) {
//                try (Socket client = server.accept();
//                     PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
//                     BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
//                    out.println("New connection accepted");
//                    String query = reader.readLine();
//                    writer.println("Hello from server! Your port is  " + client.getPort());
//                    System.out.println("Подключен клиент " + client.getPort());
//
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // здесь создайте сервер, который отвечал бы на нужные запросы
        // слушать он должен порт 8989
        // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате
    }
}