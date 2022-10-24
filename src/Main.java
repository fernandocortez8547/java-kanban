import tasks.*;
import manager.*;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        Task task = new Task(0, "Test addNewTask", "Test addNewTask description", TaskStatus.NEW,
                LocalDateTime.now().minusMinutes(60), 60
        );
        TaskManager taskManager = new InMemoryTaskManager();
        taskManager.add(task);
        Epic epic = new Epic(0, "Test addNewEpic", "Test addNewEpic description",
                TaskStatus.NEW, LocalDateTime.now(), 60
        );
        taskManager.add(epic);

        SubTask subTask = new SubTask(0, "SubTask name 1", "SubTask description", TaskStatus.NEW,
                epic.getId(), LocalDateTime.now().plusMinutes(60), 60);
        SubTask subTask1 = new SubTask(0, "SubTask name 2", "SubTask description", TaskStatus.NEW,
                epic.getId(), LocalDateTime.now().plusMinutes(120), 60);
        SubTask subTask2 = new SubTask(0, "SubTask name 3", "SubTask description", TaskStatus.NEW,
                epic.getId(), LocalDateTime.now().plusMinutes(180), 60);

        taskManager.add(subTask);
        taskManager.add(subTask1);
        taskManager.add(subTask2);

        subTask.setStartTime(LocalDateTime.now().plusMinutes(241));
        taskManager.update(subTask);
        task.setStartTime(LocalDateTime.now().plusMinutes(360));
        taskManager.update(task);

        Task task2 = new Task(0, "Test 2 addNewTask", "Test 2 addNewTask description", TaskStatus.NEW,
                LocalDateTime.now().plusMinutes(670), 60
        );

        taskManager.add(task2);
        task2.setStartTime(task2.getStartTime().plusMinutes(60));
        taskManager.update(task2);
        System.out.println(taskManager.getPrioritizedTasks());

        task2.setStartTime(task2.getStartTime().plusMinutes(60));
        taskManager.update(task2);

//        Task task3 = new Task(0, "Task 3", "Таск с пересекающимся временем", TaskStatus.NEW,
//                LocalDateTime.now().plusMinutes(120), 60);
//        taskManager.add(task3);
    }
}
