package localhost.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import localhost.LocalDateAdapter;
import manager.Manager;
import manager.TaskManager;
import tasks.*;
import util.FileConverter;

import static localhost.HttpTaskServer.*;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;

import static localhost.HttpTaskServer.*;
import static util.FileConverter.gsonToTask;
import static util.FileConverter.statusConverter;

public class TaskHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange h) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();

        String query = h.getRequestURI().getQuery();
        currentClass = "task";
        String response;
        Task task;
        try {
            switch (h.getRequestMethod()) {
                case "POST":
                    String gsonTask = new String(
                            h.getRequestBody().readAllBytes(),
                            DEFAULT_CHARSET
                    );
                    JsonElement json = JsonParser.parseString(gsonTask);
                    JsonObject jsonObject = json.getAsJsonObject();
                    int id = jsonObject.get("id").getAsInt();

                    if (taskManager.getTask(id) != null) {
                        TaskStatus status = statusConverter(jsonObject.get("status").getAsString());
                        task = taskManager.getTask(id);
                        task.setStatus(status);
                        id = taskManager.update(task);
                        response = "Успешное обновление таска! ID - " + id;
                    } else {
                        task = FileConverter.gsonToTask(jsonObject, FORMATTER, currentClass);
                        id = taskManager.add(task);
                        response = "" + id;
                    }
                    break;
                case "GET":
                    if (query == null) {
                        List<Task> tasks = taskManager.getAllTasks();
                        response = gson.toJson(tasks);
                    } else {
                        id = Integer.parseInt(query.split("=")[1]);
                        task = taskManager.getTask(id);
                        response = gson.toJson(task);
                    }
                    break;
                case "DELETE":
                    if (query == null) {
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
            h.sendResponseHeaders(200, 0);

            try (OutputStream os = h.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IOException e) {
            System.out.println("Ошибка");
            e.printStackTrace();
        }


    }
}
