package localhost;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.URI;

import com.google.gson.*;

import java.io.IOException;
import java.io.OutputStream;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.time.format.DateTimeFormatter;

import java.util.List;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import manager.*;
import tasks.TaskStatus;

import static util.FileConverter.gsonToTask;
import static util.FileConverter.statusConverter;

public class HttpTaskServer {

    protected static TaskManager taskManager = Manager.getDefaultTask();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");

    public static String currentClass;
    public static void main(String[] args) throws IOException {
        startServer();
    }
    static void startServer () throws IOException {

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8081), 0);
        HttpHandler managerHandler = (HttpExchange httpExchange) -> {

          httpExchange.sendResponseHeaders(200, 0);
          try (OutputStream os = httpExchange.getResponseBody()){
              os.write("Glad too see you. Use path 'task' that add.".getBytes());
          }
        };
        httpServer.createContext("/tasks", managerHandler);

        HttpHandler taskHandler = (HttpExchange httpExchange) -> {
            Gson gson = new Gson();
            URI uri = httpExchange.getRequestURI();
            String query = uri.getQuery();
            currentClass = "task";
            String method = httpExchange.getRequestMethod();
            String response = "";
            Task task;

            switch(method) {
                case "POST":
                    String gsonTask = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                    JsonElement json = JsonParser.parseString(gsonTask);
                    JsonObject jsonObject = json.getAsJsonObject();
                    int id = jsonObject.get("id").getAsInt();

                    if(taskManager.getTask(id) != null) {
                        TaskStatus status = statusConverter(jsonObject.get("status").getAsString());
                        task = taskManager.getTask(id);
                        task.setStatus(status);
                        id = taskManager.update(task);
                        response = "Успешное обновление таска! ID - " + id;
                    } else {
                        task = gsonToTask(jsonObject, FORMATTER, currentClass);
                        id = taskManager.add(task);
                        response = "Успешное добавление Таска! ID - " + id;
                    }
                    break;
                case "GET":
                    if(query == null) {
                        List<Task> tasks = taskManager.getAllTasks();
                        response = gson.toJson(tasks);
                    } else {
                        id = Integer.parseInt(query.split("=")[1]);
                        task = taskManager.getTask(id);
                        response = gson.toJson(task);
                    }
                    break;
                case "DELETE":
                    if(query == null) {
                        taskManager.clearingTasks();
                        response = gson.toJson("Задачи удалены");
                    } else {
                        id = Integer.parseInt(query.split("=")[1]);
                        taskManager.removeTask(id);
                        response = "Задача удалена.";
                    }
                    break;
                default:
                    response = "Неверный путь.";
                    break;
            }

            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        };
        httpServer.createContext("/tasks/task", taskHandler);

        HttpHandler epicHandler = (HttpExchange httpExchange) -> {
            Gson gson = new Gson();
            URI uri = httpExchange.getRequestURI();
            String query = uri.getQuery();
            currentClass = "epic";
            String method = httpExchange.getRequestMethod();
            String response = "";
            Epic epic;
            switch(method) {
                case "POST":
                    String gsonTask = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                    JsonElement json = JsonParser.parseString(gsonTask);
                    epic = (Epic) gsonToTask(json.getAsJsonObject(), FORMATTER, currentClass);
                    int id = taskManager.add(epic);
                    response = "Успешное добавление Эпика! ID - " + id;
                    break;
                case "GET":
                    if(query == null) {
                        List<Epic> epics = taskManager.getAllEpics();
                        response = gson.toJson(epics);
                    } else {
                        id = Integer.parseInt(query.split("=")[1]);
                        epic = taskManager.getEpic(id);
                        response = gson.toJson(epic);
                    }
                    break;
                case "DELETE":
                    if(query == null) {
                        taskManager.clearingEpics();
                        response = gson.toJson("Эпики удалены");
                    } else {
                        id = Integer.parseInt(query.split("=")[1]);
                        taskManager.removeEpic(id);
                        response = "Эпик удален.";
                    }
                    break;
                default:
                    response = "Неверный путь.";
                    break;
            }

            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        };
        httpServer.createContext("/tasks/epic", epicHandler);

        HttpHandler subTaskHandler = (HttpExchange httpExchange) -> {
            Gson gson = new Gson();
            URI uri = httpExchange.getRequestURI();
            String query = uri.getQuery();
            currentClass = "subtask";
            String method = httpExchange.getRequestMethod();
            String response = "";
            SubTask subTask;
            switch(method) {
                case "POST":
                    String gsonSubTask = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                    JsonElement json = JsonParser.parseString(gsonSubTask);
                    JsonObject jsonObject = json.getAsJsonObject();
                    int id = jsonObject.get("id").getAsInt();
                    subTask = taskManager.getSubTask(id);

                    if(subTask != null) {
                        TaskStatus status = statusConverter(jsonObject.get("status").getAsString());
                        subTask.setStatus(status);
                        taskManager.update(subTask);
                        response = "Успешное обновление подзадачи. ID - " + id;
                    } else {
                        subTask = (SubTask) gsonToTask(json.getAsJsonObject(), FORMATTER, currentClass);
                        id = taskManager.add(subTask);
                        response = "Успешное добавление подзадачи ID - " + id;
                    }
                    break;
                case "GET":
                    if(query == null) {
                        List<SubTask> subTasks = taskManager.getAllSubTasks();
                        response = gson.toJson(subTasks);
                    } else {
                        id = Integer.parseInt(query.split("=")[1]);
                        subTask = taskManager.getSubTask(id);
                        response = gson.toJson(subTask);
                    }
                    break;
                case "DELETE":
                    if(query == null) {
                        taskManager.clearingSubTasks();
                        response = gson.toJson("Подзадачи удалены");
                    } else {
                        id = Integer.parseInt(query.split("=")[1]);
                        taskManager.removeSubTask(id);
                        response = "Подзадача удалена.";
                    }
                    break;
                default:
                    response = "Неверный путь.";
                    break;
            }

            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        };
        httpServer.createContext("/tasks/subtask", subTaskHandler);
        httpServer.start();
    }
}
