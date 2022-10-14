package manager;

import tasks.*;
import java.util.List;

public interface TaskManager {

    int add(Task task);

    int add(Epic epic);

    int add(SubTask subTask);

    int update(Task task);

    int update(Epic epic);

    int update(SubTask subTask);

    List<Task> getHistory();

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTasks();

    Task getTask(int key);

    Epic getEpic(int key);

    List<SubTask> getEpicSubTasks(int epicId);

    SubTask getSubTask(int key);

    void clearingTasks();

    void clearingEpics();

    void clearingSubTasks();

    int removeTask(int taskId);

    int removeEpic(int EpicId);

    int removeSubTask(int subTaskId);
}
