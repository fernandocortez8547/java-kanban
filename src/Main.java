import Tasks.*;
import Manager.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Task task = new Task(0, "sdf", "sada", TaskStatus.NEW);
        TaskManager taskManager = new InMemoryTaskManager();
        taskManager.add(task);

        System.out.println(taskManager.getTask(task.getId()));
    }
}
