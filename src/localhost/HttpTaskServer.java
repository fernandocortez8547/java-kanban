package localhost;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.time.format.DateTimeFormatter;


import localhost.handlers.*;
import manager.*;


public class HttpTaskServer {

    public static TaskManager taskManager = Manager.getDefaultTask();
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");

    public static String currentClass;
    private static HttpServer httpServer = null;
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
