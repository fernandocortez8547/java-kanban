package localhost.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.SubTask;
import tasks.TaskStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import static util.FileConverter.*;


public class SubTaskHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange h) throws IOException {
        TaskManager taskManager = new HttpTaskServer().getHandlerTaskManager();
        SubTask subTask;

        URI uri = h.getRequestURI();

        String query = uri.getQuery();
        String method = h.getRequestMethod();
        String response;


        switch(method) {
            case "POST":
                String gsonSubTask = new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
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
                    subTask = GSON.fromJson(gsonSubTask, SubTask.class);
                    id = taskManager.add(subTask);
                    response = "" + id;
                }
                break;
            case "GET":
                if(uri.getPath().contains("epic")) {
                    id = Integer.parseInt(query.split("=")[1]);
                    List<SubTask> subTasks = taskManager.getEpicSubTasks(id);
                    response = GSON.toJson(subTasks);
                } else if(query == null) {
                    List<SubTask> subTasks = taskManager.getAllSubTasks();
                    response = GSON.toJson(subTasks);
                } else {
                    id = Integer.parseInt(query.split("=")[1]);
                    subTask = taskManager.getSubTask(id);
                    response = GSON.toJson(subTask);
                }
                break;
            case "DELETE":
                if(query == null) {
                    taskManager.clearingSubTasks();
                    response = GSON.toJson("Подзадачи удалены");
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

        h.sendResponseHeaders(200, 0);

        try (OutputStream os = h.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
