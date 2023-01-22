package tests;

import com.google.gson.*;
import localhost.HttpTaskServer;
import localhost.KVServer;
import localhost.LocalDateAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;


import static com.google.gson.JsonParser.parseString;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class HttpTaskServerTest {

    String strUrl = "http://localhost:8081/tasks";
    static KVServer server;
    static HttpTaskServer taskServer;
    static Gson gson;

    @BeforeAll
    static void startServer() {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
            gson = gsonBuilder.create();
            server = new KVServer();
            server.start();
            taskServer = new HttpTaskServer();
            taskServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void stopServer() {
        server.stop();
        taskServer.stop();
    }

    @Test
    void getTasksTest() {
        Task task = new Task(0, "Task", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/task"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/task"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonArray taskArray = parseString(response.body().toString()).getAsJsonArray();
            assertEquals(1, taskArray.size());
        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getEpicsTest() {
        Epic epic = new Epic(0, "Epic", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        try {
            URI url = URI.create(strUrl + "/epic");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonArray epicArray = parseString(response.body().toString()).getAsJsonArray();
            assertEquals(1, epicArray.size());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getSubTasksTest() {
        Epic epic = new Epic(0, "Epic", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        try {
            URI url = URI.create(strUrl + "/epic");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            SubTask subTask = new SubTask(0, "SubTask", "Description",
                    TaskStatus.NEW, Integer.parseInt(response.body()),
                    LocalDateTime.now(), 60);
            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/subtask"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask)))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/subTask"))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonArray subTaskArray = parseString(response.body()).getAsJsonArray();
            assertEquals(1, subTaskArray.size());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    void getTaskTest() {
        Task task = new Task(0, "Task", "Description", TaskStatus.NEW, LocalDateTime.now(), 60);
        try {
            URI url = URI.create(strUrl + "/task");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            task.setId(Integer.parseInt(response.body()));
            url = URI.create(strUrl + "/tasks/task?id=" + response.body());
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Task gsonTask = gson.fromJson(response.body(), Task.class);
            assertEquals(task, gsonTask);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Test
    void getEpicTest() {
        Epic epic = new Epic(0, "Epic", "Description", TaskStatus.NEW, LocalDateTime.now(), 60);
        try {
            URI url = URI.create(strUrl + "/epic");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            epic.setId(Integer.parseInt(response.body()));
            url = URI.create(strUrl + "/epic?id=" + response.body());
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Epic gsonEpic = gson.fromJson(response.body(), Epic.class);
            assertEquals(epic, gsonEpic);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getSubTaskTest() {
        Epic epic = new Epic(0, "Epic", "Description", TaskStatus.NEW, LocalDateTime.now(), 60);
        try {
            URI url = URI.create(strUrl + "/epic");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            SubTask subTask = new SubTask(0, "SubTask", "Description",
                    TaskStatus.NEW, Integer.parseInt(response.body()),
                    LocalDateTime.now(), 60);
            url = URI.create(strUrl + "/subtask");
            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/subtask"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask)))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            int id = Integer.parseInt(response.body());
            subTask.setId(id);
            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/subtask?id=" + id))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            SubTask gsonSubTask = gson.fromJson(response.body(), SubTask.class);
            assertEquals(subTask, gsonSubTask);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteTasksTest() {
        Task task = new Task(0, "Task", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/task"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            client.send(request, HttpResponse.BodyHandlers.ofString());

            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/task"))
                    .DELETE()
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            request = HttpRequest.newBuilder()
                            .uri(URI.create(strUrl + "/task"))
                            .GET()
                            .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonArray jsonArray = JsonParser.parseString((String) response.body()).getAsJsonArray();
            assertEquals(0, jsonArray.size());
        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteEpicsTest() {
        Epic epic = new Epic(0, "Epic", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        try {
            URI url = URI.create(strUrl + "/epic");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/epic"))
                    .DELETE()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonArray epicArray = parseString(response.body()).getAsJsonArray();
            assertEquals(0, epicArray.size());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteSubTasksTest() {
        Epic epic = new Epic(0, "Epic", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        try {
            URI url = URI.create(strUrl + "/epic");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            SubTask subTask = new SubTask(0, "SubTask", "Description",
                    TaskStatus.NEW, Integer.parseInt(response.body()),
                    LocalDateTime.now(), 60);
            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/subtask"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask)))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/subtask"))
                    .DELETE()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/subTask"))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonArray subTaskArray = parseString(response.body()).getAsJsonArray();
            assertEquals(0, subTaskArray.size());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getHistoryTest() {
        Task task = new Task(0, "Task", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/task"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/task?id=" + Integer.parseInt(response.body())))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/history"))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonArray jsonHistory = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(1, jsonHistory.size());
        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteTaskTest() {
        Task task = new Task(0, "Task", "Description", TaskStatus.NEW, LocalDateTime.now(), 60);
        try {
            URI url = URI.create(strUrl + "/task");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            task.setId(Integer.parseInt(response.body()));
            url = URI.create(strUrl + "/tasks/task?id=" + response.body());
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .DELETE()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("Задача удалена.", response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteEpicTest() {
        Epic epic = new Epic(0, "Epic", "Description", TaskStatus.NEW, LocalDateTime.now(), 60);
        try {
            URI url = URI.create(strUrl + "/epic");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            epic.setId(Integer.parseInt(response.body()));
            url = URI.create(strUrl + "/epic?id=" + response.body());
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .DELETE()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("Эпик удалён.", response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteSubTaskTest() {
        Epic epic = new Epic(0, "Epic", "Description", TaskStatus.NEW, LocalDateTime.now(), 60);
        try {
            URI url = URI.create(strUrl + "/epic");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            SubTask subTask = new SubTask(0, "SubTask", "Description",
                    TaskStatus.NEW, Integer.parseInt(response.body()),
                    LocalDateTime.now(), 60);
            url = URI.create(strUrl + "/subtask");
            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/subtask"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask)))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            int id = Integer.parseInt(response.body());
            subTask.setId(id);
            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/subtask?id=" + id))
                    .DELETE()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals("Подзадача удалена.", response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAllTasks() {
        Task task = new Task(0, "Task", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        Epic epic = new Epic(0, "Epic", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/task"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/epic"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            SubTask subTask = new SubTask(0, "SubTask", "Description",
                    TaskStatus.NEW, Integer.parseInt(response.body()),
                    LocalDateTime.now().plusMinutes(60), 60);
            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/subtask"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask)))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
            System.out.println(jsonArray);
            assertEquals(2, jsonArray.size());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
