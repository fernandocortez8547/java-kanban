import Tasks.*;
import Manager.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Task task = new Task(0, "Task", "Task description 1", TaskStatus.NEW);
        TaskManager taskManager = new InMemoryTaskManager();
        taskManager.add(task);
        taskManager.getTask(task.getId());

        Task task1 = new Task(0, "Task 1", "Task description 2", TaskStatus.NEW);
        taskManager.add(task1);
        taskManager.getTask(task1.getId());

        Task task2 = new Task(0, "Task 2", "Task description 3", TaskStatus.NEW);
        taskManager.add(task2);
        taskManager.getTask(task2.getId());

        System.out.println(taskManager.getHistory());

        taskManager.getTask(task1.getId());

        System.out.println(taskManager.getHistory());
    }
}
