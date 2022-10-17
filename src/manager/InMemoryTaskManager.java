package manager;

import tasks.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private static int nextId = 1;

    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, SubTask> subTasks = new HashMap<>();
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected HistoryManager historyManager = Manager.getDefaultHistory();
    protected Set<Task> prioritaizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private int idGeneration() {
        return nextId++;
    }

    @Override
    public int add(Task task) {
        if(tasks.containsValue(task) || task == null) {
            return 0;
        }
        task.setId(idGeneration());
        tasks.put(task.getId(), task);
        setPrioritaizedTasks(task);

        return task.getId();
    }

    @Override
    public int add(Epic epic) {
        if(epics.containsValue(epic) || epic == null) {
            return 0;
        }
        epic.setId(idGeneration());
        epics.put(epic.getId(), epic);
        if (epic.getSubIds().size() == 0) {
            epic.setStatus(TaskStatus.NEW);
        }

        return epic.getId();
    }

    @Override
    public int add(SubTask subTask) {
        if(subTasks.containsValue(subTask) || subTask == null) {
            return 0;
        }
        subTask.setId(idGeneration());
        Epic epic = epics.get(subTask.getEpicId()); //getEpic(subTask.getEpicId());
        epic.addSubTaskId(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
        updateStatus(subTask.getEpicId());

        setPrioritaizedTasks(subTask);
        calculateEpicTime(epic.getId());

        return subTask.getId();
    }

    public void setPrioritaizedTasks(Task task) {
        prioritaizedTasks.add(task);
        validateTasks();
    }

    private void updateStatus(int epicId) {
        int curNew = 0;
        int curDone = 0;
        Epic epic = epics.get(epicId);
        List<Integer> subIds = epic.getSubIds();
        for (int i : subIds) {
            if (subTasks.containsKey(i)) {
                SubTask s = subTasks.get(i);
                if (s.getEpicId() == epicId && s.getStatus().equals(TaskStatus.NEW)) {
                    curNew++;
                } else if (s.getEpicId() == epicId && s.getStatus().equals(TaskStatus.DONE)) {
                    curDone++;
                }
            }
        }

        if (epic.getSubIds().size() == 0 || epic.getSubIds().size() == curNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if (epic.getSubIds().size() == curDone) {
            epic.setStatus(TaskStatus.DONE);
        } else epic.setStatus(TaskStatus.IN_PROGRESS);
    }

    @Override
    public int update(Task task) {
        if(task == null) {
            return 0;
        }

        if (tasks.containsKey(task)) {
            tasks.put(task.getId(), task);
        }
        return task.getId();
    }

    @Override
    public int update(Epic epic) {
        if(epic == null) {
            return 0;
        }
        String name = epic.getName();
        String description = epic.getDescription();
        epics.get(epic.getId()).setName(name);
        epics.get(epic.getId()).setDescription(description);
        return epic.getId();
    }

    @Override
    public int update(SubTask subTask) {
        if(subTask == null) {
            return 0;
        }

        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
        }
        updateStatus(subTask.getEpicId());
        return subTask.getId();
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
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

    public Epic getEpic(int key) {
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

    public SubTask getSubTask(int key) {
        SubTask subTask = null;
        try {
            subTask = (SubTask) subTasks.get(key).clone();
            historyManager.add(subTask);
            calculateEpicTime(subTask.getEpicId());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.print("id " + key + " - ");
        }
        return subTask;
    }

    @Override
    public List<SubTask> getEpicSubTasks(int epicId) {
        List<SubTask> epicSubTasks = new ArrayList<>();
            for (int key : epics.get(epicId).getSubIds()) {
                epicSubTasks.add(subTasks.get(key));
            }
        return epicSubTasks;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritaizedTasks;
    }

    @Override
    public void clearingTasks() {
        for(int id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    public void clearingEpics() {
        for(int id : epics.keySet()) {
            clearingEpicSubtasks(id);
            historyManager.remove(id);
        }
        epics.clear();
    }

    @Override
    public void clearingSubTasks() {
        subTasks.clear();
        for (int EpicId : epics.keySet()) {
            Epic epic = epics.get(EpicId);
            epic.clearingSubIds();
            clearingEpicSubtasks(EpicId);
            updateStatus(EpicId);
        }
    }

    @Override
    public int removeTask(int taskId) {
        if(tasks.containsKey(taskId)) {
            tasks.remove(taskId);
            historyManager.remove(taskId);
            return taskId;
        }

        return 0;
    }

    @Override
    public int removeEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            clearingEpicSubtasks(epicId);
            epics.remove(epicId);
            historyManager.remove(epicId);
            return epicId;
        }

        return 0;
    }

    @Override
    public int removeSubTask(int subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            int epicId = subTasks.get(subTaskId).getEpicId();
            epics.get(epicId).removeSubTaskId(subTaskId);
            subTasks.remove(subTaskId);
            updateStatus(epicId);
            historyManager.remove(subTaskId);
            return subTaskId;
        }

        return 0;
    }

    private void clearingEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        for (int id : epic.getSubIds()) {
            subTasks.remove(id);
            historyManager.remove(id);
        }
    }

    public void calculateEpicTime(int epicId) {
        Epic epic = epics.get(epicId);
        List<SubTask> epicSubTasks = getEpicSubTasks(epicId);

        LocalDateTime startTime = null;
        long subTaskDurationSum = 0;

        for(SubTask subTask : epicSubTasks) {
            if(startTime == null || subTask.getStartTime().isBefore(startTime)) {
                startTime = subTask.getStartTime();
            }
            subTaskDurationSum += subTask.getDuration();
        }


        epic.setStartTime(startTime);
        prioritaizedTasks.add(epic);
        epic.setDuration(subTaskDurationSum);
        epic.calculateEndTime();
    }

    private boolean validateTime(Set<Task> sortTasks, Task task) {
        boolean isValidate = false;
        for(Task itTask : sortTasks) {
            if(!task.equals(itTask) && task.getStartTime().equals(itTask.getStartTime())) {
                return true;
            }
        }
        return isValidate;
    }
    public void validateTasks() {
        Set<Task> sortTasks = getPrioritizedTasks();
        for(Task task : sortTasks) {
            if(validateTime(sortTasks, task)) {
                throw new ManagerValidateException("Время задачи " + task.getId() + " пересекается.");
            }
        }
    }
}

