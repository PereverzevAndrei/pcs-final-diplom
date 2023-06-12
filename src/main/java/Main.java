import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.System.out;
import static java.lang.System.setOut;

public class Main {
    public static void main(String[] args) throws Exception {
        String searchQuery;
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        List<PageEntry> searchResult;

        try (ServerSocket server = new ServerSocket(8989)) {
            while (true) {
                try (Socket client = server.accept();
                     PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                    out.println("New connection accepted");
                    searchQuery = reader.readLine();
                    searchResult = engine.search(searchQuery);
                    Collections.sort(searchResult);
                    if (searchResult.size() == 0) {
                        writer.println("Запрашиваемое слово не найдено");
                    }
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.setPrettyPrinting().create();
                    writer.println(gson.toJson(searchResult));
                    out.println(gson.toJson(searchResult));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}