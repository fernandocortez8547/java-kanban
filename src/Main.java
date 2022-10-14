import tasks.*;
import manager.*;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();

        Epic epic = new Epic(0, "Epic", "EpicDescription", TaskStatus.NEW);
        manager.add(epic);

        SubTask subTask = new SubTask(0, "Subtask", "SubTask description", TaskStatus.DONE,
                epic.getId());

        manager.add(subTask);
        manager.clearingEpics();
        System.out.println(manager.getAllSubTasks());

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("dsfsdfdsf");
        Task task = new Task(0, "sdasd", "asdsad", TaskStatus.NEW);
        fileBackedTasksManager.add(task);

        FileBackedTasksManager newFile = new FileBackedTasksManager("dsfsdfdsf");
        newFile.repairFromFile(new File("dsfsdfdsf"));
        System.out.println(newFile.getAllTasks());
    }
}
