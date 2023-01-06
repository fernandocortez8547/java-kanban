package localhost;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static localhost.HttpTaskServer.*;
public class HistoryHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Gson gson = new Gson();
        List<Task> historyList = taskManager.getHistory();
        String response = gson.toJson(historyList);

        exchange.sendResponseHeaders(200, 0);
        try(OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
