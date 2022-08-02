package Manager;
import Tasks.*;
import java.util.*;
public class TaskManager {
    private static int nextId = 1;

    private Map<Integer, Epic> epics =  new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Task> tasks = new HashMap<>();

    private int idGeneration() {
        return nextId++;
    }

    public int add(Task task) {
        task.setId(idGeneration());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public int add(Epic epic) {
        epic.setId(idGeneration());
        epics.put(epic.getId(), epic);
        if(epic.getSubIds().size() == 0) {
            epic.setStatus("new");
        }
        return epic.getId();
    }

    public int add(SubTask subTask) {
        subTask.setId(idGeneration());
        Epic epic = returnEpic(subTask.getEpicId());
        epic.setSubIds(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
        updateStatus(subTask.getEpicId());
        return subTask.getId();
    }

    private void updateStatus(int epicId) {
        int curNew = 0;
        int curDone = 0;
        Epic epic = epics.get(epicId);
        List<Integer> subIds = epic.getSubIds();
        for(int i : subIds) {
            if(subTasks.containsKey(i)) {
                SubTask s = subTasks.get(i);
                if(s.getEpicId() == epicId && s.getStatus().equals("new")) {
                    curNew++;
                } else if(s.getEpicId() == epicId && s.getStatus().equals("done")) {
                    curDone++;
                }
            }
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

    public int updateEpicPublicFields(Epic epic) {
        String name = epic.getName();
        String description = epic.getDescription();
        epics.get(epic.getId()).setName(name);
        epics.get(epic.getId()).setDescription(description);
        return epic.getId();
    }

    public int update(SubTask subTask) {
        if(subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
        }
        updateStatus(subTask.getEpicId());
        return subTask.getId();
    }

    public List<Task> returnAllTasks() {
        List<Task> taskList = new ArrayList<>();
        for(int key : tasks.keySet()) {
            taskList.add(tasks.get(key));
        }
        return taskList;
    }

    public Task returnTask(int key) {
        Task task = null;
        try {
            task = (Task) tasks.get(key).clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.print("id " + key + " ");
        }
        return task;
    }

    public List<Epic> returnAllEpics() {
        List<Epic> epicList = new ArrayList<>();
        for(int id : epics.keySet()) {
            epicList.add(epics.get(id));
        }
        return epicList;
    }

    public Epic returnEpic (int key) {
        Epic epic = null;
        try {
            epic = (Epic) epics.get(key).clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.print("id " + key + " ");
        }
        return epic;
    }

    public List<SubTask> returnAllSubTasks() {
        List<SubTask> subTaskList = new ArrayList<>();

        for(int id : subTasks.keySet()) {
            subTaskList.add(subTasks.get(id));
        }
        return subTaskList;
    }

    public List<SubTask> returnEpicSubTasks(int epicId) {
        List<SubTask> epicSubTasks = new ArrayList<>();
        List<Integer> subIds = epics.get(epicId).getSubIds();

        for(int key : subIds) {
            epicSubTasks.add(subTasks.get(key));
        }
        return epicSubTasks;
    }

    public SubTask returnSubTask(int key) {
        return subTasks.get(key);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
        for(int EpicId : epics.keySet()) {
            Epic epic = epics.get(EpicId);
            epic.deleteSubIds();
            updateStatus(EpicId);
        }
    }

    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteEpic(int EpicId) {
        epics.remove(EpicId);
        for(int i : subTasks.keySet()) {
            SubTask s = subTasks.get(i);
            if(s.getEpicId() == EpicId) {
                subTasks.remove(i);
            }
        }
    }

    public void deleteSubEpics(int subId) {
        for(SubTask s : subTasks.values()) {
            if(s.getEpicId() == subId) {
                subTasks.remove(subId);
            }
        }
        updateStatus(subId);
    }
}

