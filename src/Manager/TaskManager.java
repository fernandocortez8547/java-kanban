package Manager;
import Tasks.*;
import java.util.*;
public class TaskManager {
    private static int nextId = 1;
    //Пока нигде не упоминали инт-сы(как и вообщем эту тему) хэш мапы и от кого хэш мапа реализуется,
    //насчет полиморфных свойств тоже пока поверхностное представление из других источников,
    //по поводу List задали Вы мне задачку) вообщем сделал ссылку на инт-с Map. Надеюсь верно

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
    public int updateEpicName(Epic epic) {
        String name = epic.getName();
        epics.get(epic.getId()).setName(name);
        return epic.getId();
    }
    public int updateEpicDescription(Epic epic) {
        String description = epic.getName();
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

    public Map<Integer, Task> returnAllTasks() {
        Map<Integer, Task> tasksClone = tasks;
        return tasksClone;
    }
    public Task returnTask(int key) {
        Task taskClone = tasks.get(key);
        return taskClone;
    }
    //надеюсь это правильная реализация, тут всего лишь ссылка, не разобрался в интернете как пользоваться clone()
    public Map<Integer, Epic> returnAllEpics() {
        Map<Integer, Epic> epicClone = epics;
        return epicClone;
    }
    public Epic returnEpic (int key) {
        Epic epicClone = epics.get(key);
        return epicClone;
    }
    public Map<Integer, SubTask> returnAllSubTasks() {
        Map<Integer, SubTask> subTasksClone = subTasks;
        return subTasksClone;
    }
    // этот метод возвращает все подзадачи конкретного эпика
    public Map<Integer, SubTask> returnEpicSubTasks(int epicId) {
        Map<Integer, SubTask> epicSubTasks = new HashMap<>();
        List<Integer> subIds = epics.get(epicId).getSubIds();
        for(int key : subIds) {
            epicSubTasks.put(key, subTasks.get(key));
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

