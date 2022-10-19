import tasks.*;
import manager.*;

import java.io.File;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
//        Task task = new Task(0, "Test addNewTask", "Test addNewTask description", TaskStatus.NEW,
//                LocalDateTime.now().minusMinutes(60), 60
//        );
        TaskManager taskManager = new InMemoryTaskManager();
//        taskManager.add(task);
//        Epic epic = new Epic(0, "Test addNewEpic", "Test addNewEpic description",
//                TaskStatus.NEW, LocalDateTime.now(), 60
//        );
//        taskManager.add(epic);
//
//        SubTask subTask = new SubTask(0, "SubTask name 1", "SubTask description", TaskStatus.NEW,
//                epic.getId(), LocalDateTime.now().plusMinutes(60), 60);
//        SubTask subTask1 = new SubTask(0, "SubTask name 2", "SubTask description", TaskStatus.NEW,
//                epic.getId(), LocalDateTime.now().plusMinutes(120), 60);
//        SubTask subTask2 = new SubTask(0, "SubTask name 3", "SubTask description", TaskStatus.NEW,
//                epic.getId(), LocalDateTime.now().plusMinutes(180), 60);
//
//        taskManager.add(subTask);
//        taskManager.add(subTask1);
//        taskManager.add(subTask2);
//
////        subTask.setStartTime(LocalDateTime.now().minusMinutes(60));
////        subTask.setDuration(120);
////        taskManager.update(subTask);
//
//
//        System.out.println(taskManager.getPrioritizedTasks());

//
        Task task = new Task(0, "Test addNewTask", "Test addNewTask description", TaskStatus.NEW,
                LocalDateTime.now().minusMinutes(60), 60
        );
        Epic epic = new Epic(0, "Test addNewEpic", "Test addNewEpic description",
                TaskStatus.NEW, LocalDateTime.now(), 60
        );
        taskManager.add(task);
        taskManager.add(epic);
        SubTask subTask = new SubTask(0, "Test addNewSubtask", "Test addNewSubTask description",
                TaskStatus.NEW, epic.getId(), LocalDateTime.now().plusMinutes(60), 60
        );
        taskManager.add(subTask);

        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubTask(subTask.getId());

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("src/TestFile.csv"));

        System.out.println(fileBackedTasksManager.getHistory());
    }
}
