package localhost.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static util.FileConverter.*;

public class EpicHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange h) throws IOException {
        TaskManager taskManager = new HttpTaskServer().getHandlerTaskManager();
        Epic epic;

        String query = h.getRequestURI().getQuery();
        String method = h.getRequestMethod();
        String response;

        switch (method) {
            case "POST":
                String gsonEpic = new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                epic = GSON.fromJson(gsonEpic, Epic.class);
                int id = taskManager.add(epic);
                response = "" + id;
                break;
            case "GET":
                if (query == null) {
                    List<Epic> epics = taskManager.getAllEpics();
                    response = GSON.toJson(epics);
                } else {
                    id = Integer.parseInt(query.split("=")[1]);
                    epic = taskManager.getEpic(id);
                    response = GSON.toJson(epic);
                }
                break;
            case "DELETE":
                if (query == null) {
                    taskManager.clearingEpics();
                    response = GSON.toJson("Эпики удалены");
                } else {
                    id = Integer.parseInt(query.split("=")[1]);
                    taskManager.removeEpic(id);
                    response = "Эпик удалён.";
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
