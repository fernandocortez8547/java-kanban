package localhost;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.URI;

import com.google.gson.*;

import java.io.IOException;
import java.io.OutputStream;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.time.format.DateTimeFormatter;

import java.util.List;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import manager.*;
import tasks.TaskStatus;

import static util.FileConverter.gsonToTask;
import static util.FileConverter.statusConverter;

public class HttpTaskServer {

    protected static TaskManager taskManager = Manager.getDefaultTask();
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");

    public static String currentClass;
    public static void main(String[] args) throws IOException {
        startServer();
    }
    static void startServer () throws IOException {

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8081), 0);

        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
        httpServer.createContext("/tasks/subtask", new SubTaskHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());

        httpServer.start();
    }
}
