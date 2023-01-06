package localhost;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import static localhost.HttpTaskServer.*;
import static util.FileConverter.gsonToTask;
import static util.FileConverter.statusConverter;

public class TaskHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Gson gson = new Gson();
        URI uri = exchange.getRequestURI();
        String query = uri.getQuery();
        currentClass = "task";
        String method = exchange.getRequestMethod();
        String response = "";
        Task task;

        switch (method) {
            case "POST":
                String gsonTask = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
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
                    task = gsonToTask(jsonObject, FORMATTER, currentClass);
                    id = taskManager.add(task);
                    response = "Успешное добавление Таска! ID - " + id;
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

        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
