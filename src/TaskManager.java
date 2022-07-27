import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    int nextId = 1;

    HashMap<Integer, Epic> epics =  new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Task> tasks = new HashMap<>();

    public int add(Task task) {
        int newId = nextId++;
        task.setId(newId);
        tasks.put(task.getId(), task);
        return newId;
    }

    public int add(Epic epic) {
        int newId = nextId++;
        epic.setId(newId);
        epics.put(epic.getId(), epic);
        return newId;
    }

    public int addSubTask(SubTask subTask) {
        int newId = nextId++;
        subTask.setId(newId);
        subTasks.put(subTask.getId(), subTask);
        return newId;
    }
    void printAll () {
        for(int i : epics.keySet()) {
            for(Epic e : epics.values()) {
                System.out.println(i + " " + e);
            }
        }
    }
}
