package Manager;
import Tasks.*;
import java.util.*;
public class InMemoryTaskManager implements TaskManager{
    private static int nextId = 1;

    private Map<Integer, Epic> epics =  new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Task> tasks = new HashMap<>();
    private HistoryManager historyManager = Manager.getDefaultHistory();

    private int idGeneration() {
        return nextId++;
    }

    @Override
    public int add(Task task) {
        task.setId(idGeneration());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int add(Epic epic) {
        epic.setId(idGeneration());
        epics.put(epic.getId(), epic);
        if(epic.getSubIds().size() == 0) {
            epic.setStatus(TaskStatus.NEW);
        }
        return epic.getId();
    }

    @Override
    //исправлено получение эпика, вместо метода getEpic, Эпик находится по ключу в мапе, чтобы не путать HistoryManager
    public int add(SubTask subTask) {
        subTask.setId(idGeneration());
        Epic epic = epics.get(subTask.getEpicId()); //getEpic(subTask.getEpicId());
        epic.addSubTaskId(subTask.getId());
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
                if(s.getEpicId() == epicId && s.getStatus().equals(TaskStatus.NEW)) {
                    curNew++;
                } else if(s.getEpicId() == epicId && s.getStatus().equals(TaskStatus.DONE)) {
                    curDone++;
                }
            }
        }

        if(epic.getSubIds().size() == 0 || epic.getSubIds().size() == curNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if(epic.getSubIds().size() == curDone) {
            epic.setStatus(TaskStatus.DONE);
        } else epic.setStatus(TaskStatus.IN_PROGRESS);
    }

    @Override
    public int update(Task task) {
        if(tasks.containsKey(task)) {
            tasks.put(task.getId(), task);
        }
        return task.getId();
    }

    @Override
    public int update(Epic epic) {
        String name = epic.getName();
        String description = epic.getDescription();
        epics.get(epic.getId()).setName(name);
        epics.get(epic.getId()).setDescription(description);
        return epic.getId();
    }

    @Override
    public int update(SubTask subTask) {
        if(subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
        }
        updateStatus(subTask.getEpicId());
        return subTask.getId();
    }

    @Override
    public List<Task> getHistory (){
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        for(int key : tasks.keySet()) {
            taskList.add(tasks.get(key));
        }
        return taskList;
    }

    @Override
    public List<Epic> getAllEpics() {
        List<Epic> epicList = new ArrayList<>();
        for(int id : epics.keySet()) {
            epicList.add(epics.get(id));
        }
        return epicList;
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        List<SubTask> subTaskList = new ArrayList<>();

        for(int id : subTasks.keySet()) {
            subTaskList.add(subTasks.get(id));
        }
        return subTaskList;
    }

    @Override
    public Task getTask(int key) {
        Task task = null;
        try {
            task = (Task) tasks.get(key).clone();
            historyManager.add(task);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.print("id " + key + " - ");
        }
        return task;
    }

    @Override
    public Epic getEpic (int key) {
        Epic epic = null;
        try {
            epic = (Epic) epics.get(key).clone();
            historyManager.add(epic);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.print("id " + key + " - ");
        }
        return epic;
    }

    @Override
    public List<SubTask> getEpicSubTasks(int epicId) {
        List<SubTask> epicSubTasks = new ArrayList<>();
        List<Integer> subIds = epics.get(epicId).getSubIds();

        for(int key : subIds) {
            epicSubTasks.add(subTasks.get(key));
        }
        if(epicSubTasks.size() == 0) {
            return null;
        }
        return epicSubTasks;
    }

    @Override
    public SubTask getSubTask(int key) {
        SubTask subTask = null;
        try {
            subTask = (SubTask) subTasks.get(key).clone();
            historyManager.add(subTask);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.print("id " + key + " - ");
        }
        return subTask;
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
        for(int EpicId : epics.keySet()) {
            Epic epic = epics.get(EpicId);
            epic.deleteSubIds();
            updateStatus(EpicId);
        }
    }

    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void deleteEpic(int EpicId) {
        epics.remove(EpicId);
        for(int i : subTasks.keySet()) {
            SubTask s = subTasks.get(i);
            if(s.getEpicId() == EpicId) {
                subTasks.remove(i);
            }
        }
    }

    @Override
    public void deleteSubTask(int subTaskId) {
        int epicId = subTasks.get(subTaskId).getEpicId();
        for(int id : subTasks.keySet()) {
            if(id == subTaskId) {
                epics.get(epicId).deleteSubTaskId(id);
                subTasks.remove(subTaskId);
            }
        }
        updateStatus(epicId);
    }

}

