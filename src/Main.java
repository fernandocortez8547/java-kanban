import tasks.*;
import manager.*;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        Epic epic = new Epic(0, "Name", "description", TaskStatus.NEW, LocalDateTime.now(),
                60);
        TaskManager taskManager = new InMemoryTaskManager();
        taskManager.add(epic);
        SubTask subTask = new SubTask(0, "Name",
                "Description",
                TaskStatus.NEW,
                epic.getId(),
                LocalDateTime.now(), 60);
        int subTaskId = taskManager.add(subTask);

        Task task = new Task(0, "Task", "Description", TaskStatus.NEW,
                LocalDateTime.now().plusMinutes(60), 60);
        taskManager.add(task);
        task.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.update(task);
        taskManager.clearingSubTasks();
        System.out.println(taskManager.getPrioritizedTasks());
    }
}
