package localhost.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.HttpTaskManager;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import static util.FileConverter.GSON;

public class TasksHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        TaskManager taskManager = new HttpTaskServer().getHandlerTaskManager();

        String method = exchange.getRequestMethod();
        String response;

        if(method.equals("GET")) {
            Set<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
            response = GSON.toJson(prioritizedTasks);
        } else {
            response = "Неверный метод";
        }

        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()){
            os.write(response.getBytes());
        }
    }
}
