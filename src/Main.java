import tasks.*;
import manager.*;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        FileBackedTasksManager manager = new FileBackedTasksManager("src" + File.separator + "TestFile.csv");

        Task task = new Task(0, "Task name", "Task Description", TaskStatus.NEW);

        Epic epic = new Epic(0, "Epic name", "Epic Description", TaskStatus.NEW);
        manager.add(epic);

        SubTask subTask = new SubTask(0, "SubTask name", "Subtask Description", TaskStatus.NEW, epic.getId());
        manager.add(subTask);

        manager.add(task);

        manager.getEpic(epic.getId());
        manager.getTask(task.getId());
        manager.getSubTask(subTask.getId());

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getHistory());

//        manager.removeEpic(epic.getId());
//        manager.removeTask(task.getId());

        System.out.println(manager);
    }
}
