package localhost.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static util.FileConverter.GSON;

public class HistoryHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange h) throws IOException {
        TaskManager taskManager = new HttpTaskServer().getHandlerTaskManager();

        List<Task> historyList = taskManager.getHistory();
        String response = GSON.toJson(historyList);

        h.sendResponseHeaders(200, 0);

        try(OutputStream os = h.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
