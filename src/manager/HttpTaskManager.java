package manager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import localhost.HttpTaskServer;
import localhost.KVTaskClient;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import util.FileConverter;

import java.io.File;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient client;
    protected Gson gson = new Gson();

    public HttpTaskManager(String url) {
        client = new KVTaskClient(url);
        load();
    }

    @Override
    public void save() throws ManagerSaveException {
        Gson gson = new Gson();
        if(!tasks.isEmpty()) {
            String gsonTasks = gson.toJson(tasks);
            client.put("tasks", gsonTasks);
        }
        if(!epics.isEmpty()) {
            String gsonEpics = gson.toJson(epics);
            client.put("epics", gsonEpics);
        }
        if(!subTasks.isEmpty()) {
            String gsonSubTasks = gson.toJson(subTasks);
            client.put("subTasks", gsonSubTasks);
        }
        List<Task> history = historyManager.getHistory();
        if(!history.isEmpty()) {
            String gsonHistory = gson.toJson(history);
            client.put("history", gsonHistory);
        }
    }

    public void load() {
        JsonElement jsonTasks = JsonParser.parseString(client.load("tasks"));
        if(!jsonTasks.isJsonNull()) {
            JsonArray tasksArray = jsonTasks.getAsJsonArray();
            for(JsonElement jsonTask : tasksArray) {
                Task task = FileConverter.gsonToTask(jsonTask.getAsJsonObject(),
                        HttpTaskServer.FORMATTER,
                        "task");
                add(task);
            }
        }
        JsonElement jsonEpics = JsonParser.parseString(client.load("epics"));
        if(!jsonEpics.isJsonNull()) {
            JsonArray epicsArray = jsonEpics.getAsJsonArray();
            for(JsonElement jsonEpic : epicsArray) {
                Epic epic = (Epic)FileConverter.gsonToTask(jsonEpic.getAsJsonObject(),
                        HttpTaskServer.FORMATTER,
                        "epic");
                add(epic);
            }
        }
        JsonElement jsonSubTasks = JsonParser.parseString(client.load("subTasks"));
        if(!jsonSubTasks.isJsonNull()) {
            JsonArray subTasksArray = jsonSubTasks.getAsJsonArray();
            for(JsonElement jsonSubTask : subTasksArray) {
                SubTask subTask = (SubTask) FileConverter.gsonToTask(jsonSubTask.getAsJsonObject(),
                        HttpTaskServer.FORMATTER,
                        "subTask");
                add(subTask);
            }
        }
        JsonElement jsonHistory = JsonParser.parseString(client.load("history"));
        if(!jsonHistory.isJsonNull()) {
            for(int id : FileConverter.historyFromString(jsonHistory.getAsString())) {
                repairHistory(id);
            }
        }
    }
}
