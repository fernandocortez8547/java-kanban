package manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import exceptions.ManagerSaveException;
import localhost.KVTaskClient;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import util.FileConverter;
import static util.FileConverter.GSON;

import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient client;
    public HttpTaskManager(String url) {
        client = new KVTaskClient(url);
        load();
    }

    @Override
    public void save() throws ManagerSaveException {
        if(!tasks.isEmpty()) {
            String gsonTasks = GSON.toJson(tasks);
            client.put("tasks", gsonTasks);
        }
        if(!epics.isEmpty()) {
            String gsonEpics = GSON.toJson(epics);
            client.put("epics", gsonEpics);
        }
        if(!subTasks.isEmpty()) {
            String gsonSubTasks = GSON.toJson(subTasks);
            client.put("subTasks", gsonSubTasks);
        }
        List<Task> history = historyManager.getHistory();
        if(!history.isEmpty()) {
            String gsonHistory = GSON.toJson(history);
            client.put("history", gsonHistory);
        }
    }

    public void load() {
        JsonElement jsonTasks = JsonParser.parseString(client.load("tasks"));
        if(!jsonTasks.isJsonNull()) {
            JsonArray tasksArray = jsonTasks.getAsJsonArray();
            for(JsonElement jsonTask : tasksArray) {
                Task task = GSON.fromJson(jsonTask.toString(), Task.class);
                add(task);
            }
        }
        JsonElement jsonEpics = JsonParser.parseString(client.load("epics"));
        if(!jsonEpics.isJsonNull()) {
            JsonArray epicsArray = jsonEpics.getAsJsonArray();
            for(JsonElement jsonEpic : epicsArray) {
                Epic epic = GSON.fromJson(jsonEpic.toString(), Epic.class);
                add(epic);
            }
        }
        JsonElement jsonSubTasks = JsonParser.parseString(client.load("subTasks"));
        if(!jsonSubTasks.isJsonNull()) {
            JsonArray subTasksArray = jsonSubTasks.getAsJsonArray();
            for(JsonElement jsonSubTask : subTasksArray) {
                SubTask subTask = GSON.fromJson(jsonSubTask.toString(), SubTask.class);
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
