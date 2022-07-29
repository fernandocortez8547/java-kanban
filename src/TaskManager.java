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
        Epic epic = returnEpic(subTask.getSupId());
        epic.setSubIds(subTask.getId());
        subTask.setId(newId);
        subTasks.put(subTask.getId(), subTask);
        updateStatus(subTask.getSupId());
        return newId;
    }

    private void updateStatus(int epicId) {
        int curNew = 0;
        int curDone = 0;
        Epic epic = epics.get(epicId);
        for(int i : subTasks.keySet()) {
            SubTask s = subTasks.get(i);
            if(s.getSupId() == epicId && s.getStatus().equals("new")) {
                curNew++;
            } else if(s.getSupId() == epicId && s.getStatus().equals("done")) {
                curDone++;
            } else continue;
        }
        if(epic.getSubIds().size() == 0 || epic.getSubIds().size() == curNew) {
            epic.setStatus("new");
        } else if(epic.getSubIds().size() == curDone) {
            epic.setStatus("done");
        } else epic.setStatus("inProgress");
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

    HashMap<Integer, Task> returnAllTasks() {
        return tasks;
    }
    Task returnTask(int key) {
        return tasks.get(key);
    }
    HashMap<Integer, Epic> returnAllEpics() {
        return epics;
    }
    Epic returnEpic (int key) {
        return epics.get(key);
    }
    HashMap<Integer, SubTask> returnAllSubTasks() {
        return subTasks;
    }
    ArrayList<SubTask> returnSubTasks(int supId) {
        ArrayList<SubTask> al = new ArrayList<>();
        for(int key : subTasks.keySet()) {
            SubTask s = subTasks.get(key);
            if(s.getSupId() == supId) {
                al.add(s);
            }
        }
        return al;
    }
    SubTask returnSubTask(int key) {
        return subTasks.get(key);
    }

    void deleteAllTasks() {
        tasks.clear();
    }
    void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }
    void deleteAllSubTasks() {
        subTasks.clear();
        for(int supId : epics.keySet()) {
            Epic epic = epics.get(supId);
            epic.deleteSubIds();
            updateStatus(supId);
        }
    }
    void deleteTask(int taskId) {
        tasks.remove(taskId);
    }
    void deleteEpic(int supId) {
        epics.remove(supId);
        for(int i : subTasks.keySet()) {
            SubTask s = subTasks.get(i);
            if(s.getSupId() == supId) {
                subTasks.remove(i);
            }
        }
    }
    void deleteSubEpics(int subId) {
        for(SubTask s : subTasks.values()) {
            if(s.getSupId() == subId) {
                subTasks.remove(subId);
            }
        }
        updateStatus(subId);
    }
}
