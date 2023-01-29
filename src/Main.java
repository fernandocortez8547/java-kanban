import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import localhost.*;
import localhost.handlers.HttpTaskServer;
import tasks.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
        KVServer server;
        try {
            server = new KVServer();
            server.start();
            HttpTaskServer taskServer = new HttpTaskServer();
            taskServer.startServer();
            Task task = new Task(0, "0", "0", TaskStatus.NEW,
                    LocalDateTime.now(), 60);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/tasks/task"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Ответ сервера на POST таска: " + response.body());

            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/tasks/task"))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Ответ сервера на GET всех тасков: " + response.body());
            server.stop();
            taskServer.stop();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
