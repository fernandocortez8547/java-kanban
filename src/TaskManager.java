import java.util.*;

public class TaskManager {
    int nextId = 1;

    private HashMap<Integer, Epic> epics =  new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Task> tasks = new HashMap<>();

    public int add(Task task) {
        int newId = nextId++;
        task.setId(newId);
        tasks.put(newId, task);
        return newId;
    }

    public int add(Epic epic) {
        int newId = nextId++;
        epic.setId(newId);
        epics.put(newId, epic);
        if(epic.getSubIds().size() == 0) {
            epic.setStatus("new");
        }
        return newId;
    }

    public int add(SubTask subTask) {
        int newId = nextId++;
        subTask.setId(newId);
        subTasks.put(subTask.getId(), subTask);
        return newId;
    }

    public int update(Task task) {
        if(tasks.containsKey(task)) {
            tasks.put(task.getId(), task);
        }
        return task.getId();
    }
    public int update(Epic epic) {
        if(epics.containsKey(epic)) {
           epics.put(epic.getId(), epic);
        }
        return epic.getId();
    }
    public int update(SubTask subTask) {
        if(subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
        }
        return subTask.getId();
    }
    HashMap<Integer, Task> returnTasks() {
        return tasks;
    }
    Task returnTask(int key) {
        return tasks.get(key);
    }
    HashMap<Integer, Epic> returnEpics() {
        return epics;
    }
    Epic returnEpic (int key) {
        return epics.get(key);
    }

    ArrayList<SubTask> returnSubTasks(Epic epic) {
        int epicId = epic.getId();
        ArrayList<SubTask> al = new ArrayList<>();

        for(int key : subTasks.keySet()) {
            SubTask s = subTasks.get(key);
            if(s.getSupId() == epicId) {
                al.add(s);
            }
        }
        return al;
    }
    HashMap<Integer, SubTask> returnSubTask() {
        return subTasks;
    }
    SubTask returnSubTask(int key) {
        return subTasks.get(key);
    }

    void deleteTasks() {
        tasks.clear();
    }
    void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }
    void deleteTask(int key) {
        tasks.remove(key);
    }
    void deleteEpic(int key) {
        tasks.remove(key);
        for(int i : subTasks.keySet()) {
            SubTask s = subTasks.get(i);
            if(s.getSupId() == key) {
                subTasks.remove(i);
            }
        }
    }
}
