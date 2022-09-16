import Tasks.*;
import Manager.*;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new FileBackedTasksManager("src" + File.separator + "TestFile.csv");

        Task task = new Task(0, "Task name", "Task Description", TaskStatus.NEW);
        Epic epic = new Epic(0, "Epic name", "Epic Description", TaskStatus.NEW);
        manager.add(epic);
        SubTask subTask = new SubTask(0, "SubTask name", "Subtask Description", TaskStatus.NEW, epic.getId());

        manager.add(task);
        manager.add(subTask);

        System.out.println(manager.getTask(task.getId()));
        System.out.println(manager.getEpic(epic.getId()));
        System.out.println(manager.getSubTask(subTask.getId()));


    }
}
