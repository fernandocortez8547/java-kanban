package localhost;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Epic;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import static localhost.HttpTaskServer.*;
import static util.FileConverter.gsonToTask;

public class EpicHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Gson gson = new Gson();
        URI uri = exchange.getRequestURI();
        String query = uri.getQuery();
        currentClass = "epic";
        String method = exchange.getRequestMethod();
        String response = "";
        Epic epic;
        switch(method) {
            case "POST":
                String gsonTask = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
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

        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
