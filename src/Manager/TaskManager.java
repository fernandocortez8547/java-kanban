package Manager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import java.util.List;

public interface TaskManager {

        int add(Task task);
        int add(Epic epic);
        int add(SubTask subTask);

        int update(Task task);
        int update(Epic epic);
        int update(SubTask subTask);

        List<SubTask> returnAllSubTasks();
        List<Task> returnAllTasks();
        List<Epic> returnAllEpics();

        Task returnTask(int key);
        Epic returnEpic (int key);
        List<SubTask> returnEpicSubTasks(int epicId);
        SubTask returnSubTask(int key);

        void deleteAllTasks();
        void deleteAllEpics();
        void deleteAllSubTasks();

        void deleteTask(int taskId);
        void deleteEpic(int EpicId);
        void deleteSubTask(int subTaskId);
}
