package localhost.handlers;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import manager.*;


public class HttpTaskServer {

    // Я понимаю опасность использования static на переменной TaskManager, но иначе в данном случае нельзя
    // Хэндлеры должны иметь общую переменную менеджера.
    // В случае, если она не принадлежит классу прийдется создавать новый экземпляр HttpTaskServer
    // у которого эта переменная тоже будет другим.

    // Сделал гетер protected, для этого пришлось переместить класс в пакет хэндлеров
    private static final TaskManager DEFAULT_TASK = Manager.getDefaultTask();
    private HttpServer httpServer = null;
    public static DateTimeFormatter HTTP_SERVER_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");

    protected TaskManager getHandlerTaskManager() {
        return DEFAULT_TASK;
    }

    public void startServer () {
        try {
            httpServer = HttpServer.create(new InetSocketAddress( 8081), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
        httpServer.createContext("/tasks/subtask", new SubTaskHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());

        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }


}
