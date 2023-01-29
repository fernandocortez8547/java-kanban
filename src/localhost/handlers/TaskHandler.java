package localhost.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.HttpTaskManager;
import manager.Manager;
import manager.TaskManager;
import tasks.*;
import util.FileConverter;


import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


import static util.FileConverter.*;

public class TaskHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange h) {
        TaskManager taskManager = new HttpTaskServer().getHandlerTaskManager();

        Task task;

        String query = h.getRequestURI().getQuery();
        String response;

        try {
            switch (h.getRequestMethod()) {
                case "POST":
                    String gsonTask = new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
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
                        task = GSON.fromJson(gsonTask, Task.class);
                        id = taskManager.add(task);
                        response = "" + id;
                    }
                    break;
                case "GET":
                    if (query == null) {
                        List<Task> tasks = taskManager.getAllTasks();
                        response = GSON.toJson(tasks);
                    } else {
                        id = Integer.parseInt(query.split("=")[1]);
                        task = taskManager.getTask(id);
                        response = GSON.toJson(task);
                    }
                    break;
                case "DELETE":
                    if (query == null) {
                        taskManager.clearingTasks();
                        response = GSON.toJson("Задачи удалены");
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
            h.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
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
