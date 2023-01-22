package localhost.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import localhost.LocalDateAdapter;
import tasks.SubTask;
import tasks.TaskStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static localhost.HttpTaskServer.*;
import static util.FileConverter.gsonToTask;
import static util.FileConverter.statusConverter;

public class SubTaskHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
        URI uri = exchange.getRequestURI();
        String query = uri.getQuery();
        currentClass = "subtask";
        String method = exchange.getRequestMethod();
        String response = "";
        SubTask subTask;
        switch(method) {
            case "POST":
                String gsonSubTask = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                JsonElement json = JsonParser.parseString(gsonSubTask);
                JsonObject jsonObject = json.getAsJsonObject();
                int id = jsonObject.get("id").getAsInt();
                subTask = taskManager.getSubTask(id);

                if (subTask != null) {
                    TaskStatus status = statusConverter(jsonObject.get("status").getAsString());
                    subTask.setStatus(status);
                    taskManager.update(subTask);
                    response = "Успешное обновление подзадачи. ID - " + id;
                } else {
                    subTask = (SubTask) gsonToTask(json.getAsJsonObject(), FORMATTER, currentClass);
                    id = taskManager.add(subTask);
                    response = "" + id;
                }
                break;
            case "GET":
                if(uri.getPath().contains("epic")) {
                    id = Integer.parseInt(query.split("=")[1]);
                    List<SubTask> subTasks = taskManager.getEpicSubTasks(id);
                    response = gson.toJson(subTasks);
                } else if(query == null) {
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

        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
