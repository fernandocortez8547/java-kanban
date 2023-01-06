package localhost;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.TreeSet;
import static localhost.HttpTaskServer.*;

public class TasksHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Gson gson = new Gson();
        String method = exchange.getRequestMethod();
        String response = "";
        if(method.equals("GET")) {
            Set<Task> prioritazedTasks = taskManager.getPrioritizedTasks();
            response = gson.toJson(prioritazedTasks);
        } else {
            response = "Неверный метод";
        }

        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()){
            os.write(response.getBytes());
        }
    }
}
