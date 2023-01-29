package tests;

import com.google.gson.*;
import localhost.handlers.HttpTaskServer;
import localhost.KVServer;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import static util.FileConverter.GSON;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import static com.google.gson.JsonParser.parseString;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class HttpTaskServerTest {
    private final String strUrl = "http://localhost:8081/tasks";
    private static HttpTaskServer taskServer;

    @BeforeAll
    static void startServer() throws IOException {
        KVServer server = new KVServer();
        server.start();
    }

    @BeforeEach
    public void clearingManagerFields() throws IOException, InterruptedException {
        taskServer = new HttpTaskServer();
        taskServer.startServer();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/tasks/task"))
                .DELETE()
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/tasks/epic"))
                .DELETE()
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/tasks/subtask"))
                .DELETE()
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @AfterEach
    void stopServer() {
        taskServer.stop();
    }

    @Test
    void getTasksTest() {
        Task task = new Task(0, "Task", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(strUrl + "/task"))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(task)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/task"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonArray taskArray = parseString(response.body()).getAsJsonArray();
            System.out.println("Размер массива: " + taskArray.size());
            assertEquals(1, taskArray.size());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при отправке запроса на сервер.");
        }
    }

    @Test
    void getEpicsTest() {
        Epic epic = new Epic(0, "Epic", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        URI url = URI.create(strUrl + "/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(epic)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonArray epicArray = parseString(response.body()).getAsJsonArray();
            assertEquals(1, epicArray.size());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при отправке запроса на сервер.");
        }
    }

    @Test
    void getSubTasksTest() {
        Epic epic = new Epic(0, "Epic", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        URI url = URI.create(strUrl + "/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(epic)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            SubTask subTask = new SubTask(0, "SubTask", "Description",
                    TaskStatus.NEW, Integer.parseInt(response.body()),
                    LocalDateTime.now(), 60);
            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/subtask"))
                    .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(subTask)))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/subTask"))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonArray subTaskArray = parseString(response.body()).getAsJsonArray();
            System.out.println("Что представляет из себя array: " + subTaskArray);
            assertEquals(1, subTaskArray.size());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при отправке запроса на сервер.");
        }
    }

    @Test
    void getTaskTest() {
        Task task = new Task(0, "Task", "Description", TaskStatus.NEW, LocalDateTime.now(), 60);

        URI url = URI.create(strUrl + "/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(task)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            task.setId(Integer.parseInt(response.body()));
            url = URI.create(strUrl + "/tasks/task?id=" + response.body());
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Task gsonTask = GSON.fromJson(response.body(), Task.class);
            assertEquals(task, gsonTask);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при отправке запроса на сервер.");
        }
    }


    @Test
    void getEpicTest() {
        Epic epic = new Epic(0, "Epic", "Description", TaskStatus.NEW, LocalDateTime.now(), 60);
        URI url = URI.create(strUrl + "/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(epic)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            epic.setId(Integer.parseInt(response.body()));
            url = URI.create(strUrl + "/epic?id=" + response.body());
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Epic gsonEpic = GSON.fromJson(response.body(), Epic.class);
            assertEquals(epic, gsonEpic);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при отправке запроса на сервер.");
        }
    }

    @Test
    void getSubTaskTest() {
        Epic epic = new Epic(0, "Epic", "Description", TaskStatus.NEW, LocalDateTime.now(), 60);
        URI url = URI.create(strUrl + "/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(epic)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            SubTask subTask = new SubTask(0, "SubTask", "Description",
                    TaskStatus.NEW, Integer.parseInt(response.body()),
                    LocalDateTime.now(), 60);
            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/subtask"))
                    .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(subTask)))
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

            SubTask gsonSubTask = GSON.fromJson(response.body(), SubTask.class);
            assertEquals(subTask, gsonSubTask);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при отправке запроса на сервер.");
        }
    }

    @Test
    void deleteTasksTest() {
        Task task = new Task(0, "Task", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
//        try {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(strUrl + "/task"))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(task)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());

            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/task"))
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/task"))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
            assertEquals(0, jsonArray.size());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при отправке запроса на сервер.");
        }
    }

    @Test
    void deleteEpicsTest() {
        Epic epic = new Epic(0, "Epic", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        URI url = URI.create(strUrl + "/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(epic)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
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
            throw new RuntimeException("Ошибка при отправке запроса на сервер.");
        }
    }

    @Test
    void deleteSubTasksTest() {
        Epic epic = new Epic(0, "Epic", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);

        URI url = URI.create(strUrl + "/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(epic)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            SubTask subTask = new SubTask(0, "SubTask", "Description",
                    TaskStatus.NEW, Integer.parseInt(response.body()),
                    LocalDateTime.now(), 60);
            request = HttpRequest.newBuilder()
                    .uri(URI.create(strUrl + "/subtask"))
                    .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(subTask)))
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
            throw new RuntimeException("Ошибка при отправке запроса на сервер.");
        }
    }

    @Test
    void getHistoryTest() {
        Task task = new Task(0, "Task", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(strUrl + "/task"))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(task)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
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
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при отправке запроса на сервер.");
        }
    }

    @Test
    void deleteTaskTest() {
        Task task = new Task(0, "Task", "Description", TaskStatus.NEW, LocalDateTime.now(), 60);
        URI url = URI.create(strUrl + "/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(task)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
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
            throw new RuntimeException("Ошибка при отправке запроса на сервер.");
        }
    }

    @Test
    void deleteEpicTest() {
        Epic epic = new Epic(0, "Epic", "Description", TaskStatus.NEW, LocalDateTime.now(), 60);

        URI url = URI.create(strUrl + "/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(epic)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
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
            throw new RuntimeException("Ошибка при отправке запроса на сервер.");
        }
    }

    @Test
    void deleteSubTaskTest() {
        Epic epic = new Epic(0, "Epic", "Description", TaskStatus.NEW, LocalDateTime.now(), 60);

        URI url = URI.create(strUrl + "/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(epic)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        SubTask subTask = new SubTask(0, "SubTask", "Description",
                TaskStatus.NEW, Integer.parseInt(response.body()),
                LocalDateTime.now(), 60);
        request = HttpRequest.newBuilder()
                .uri(URI.create(strUrl + "/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(subTask)))
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
            throw new RuntimeException("Ошибка при отправке запроса на сервер.");
        }
    }

    @Test
    void getAllTasks() {
        Task task = new Task(0, "Task", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        Epic epic = new Epic(0, "Epic", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(strUrl + "/task"))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(task)))
                .build();
        try {
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder()
                .uri(URI.create(strUrl + "/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(epic)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask = new SubTask(0, "SubTask", "Description",
                TaskStatus.NEW, Integer.parseInt(response.body()),
                LocalDateTime.now().plusMinutes(60), 60);
        request = HttpRequest.newBuilder()
                .uri(URI.create(strUrl + "/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(subTask)))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(URI.create(strUrl))
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
        System.out.println(jsonArray);
        assertEquals(2, jsonArray.size());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при отправке запроса на сервер.");
        }
    }
}
