import tasks.*;
import manager.*;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class Main {

    public static void main(String[] args) {


//        Task task = new Task(0, "Task name", "Task description", TaskStatus.NEW,
//                LocalDateTime.now(), 60);
        Epic epic = new Epic(0, "Epic name", "Epic Description", TaskStatus.NEW,
                LocalDateTime.now(), 0);

        TaskManager taskManager = new InMemoryTaskManager();
        taskManager.add(epic);

        SubTask subTask1 = new SubTask(0, "SubTask name", "Subtask description", TaskStatus.NEW,
                epic.getId(), LocalDateTime.now().plusMinutes(60), 60);

        SubTask subTask2 = new SubTask(0, "SubTask 2 name", "Subtask description", TaskStatus.NEW,
                epic.getId(), LocalDateTime.now(), 60);

        SubTask subTask3 = new SubTask(0, "SubTask 3 name", "Subtask description", TaskStatus.NEW,
                epic.getId(), LocalDateTime.now().minusMinutes(60), 60);

        taskManager.add(subTask1);
        taskManager.add(subTask2);
        taskManager.add(subTask3);

        System.out.println(taskManager.getPrioritizedTasks());

    }
}
