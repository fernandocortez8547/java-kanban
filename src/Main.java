import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import localhost.*;
import tasks.*;
import manager.*;

import java.io.IOException;
import java.net.HttpRetryException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
        KVServer server;
        try {
            server = new KVServer();
            server.start();
//            TaskManager manager = Manager.getDefaultTask();
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
            System.out.println(response.body());
            server.stop();
            taskServer.stop();
//            Task task = new Task(0,
//                    "Task",
//                    "Description",
//                    TaskStatus.NEW,
//                    LocalDateTime.now(),
//                    60);
//            Epic epic = new Epic(0,
//                    "Epic",
//                    "Description",
//                    TaskStatus.NEW,
//                    LocalDateTime.now(),
//                    60);
//
//            manager.add(task);
//            manager.add(epic);
//
//            SubTask subTask = new SubTask(0,
//                    "SubTask",
//                    "Description",
//                    TaskStatus.NEW,
//                    epic.getId(),
//                    LocalDateTime.now().plusMinutes(60),
//                    60);
//
//            manager.add(subTask);
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//            System.out.println("Ошибка ввода при запуске KVServer");
//        }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
