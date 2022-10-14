package tests;

import manager.FileBackedTasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.File;
import java.io.FileReader;

public class FileBackedTasksManagerTest  extends TaskManagerTest{
    public static final String PATH = "tasks_test.csv";
    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTasksManager(PATH);
    }

    @Test
    public void loadAndSaveTest() {
        Task task = getTask();
        taskManager.add(task);
        Epic epic = getEpic();
        taskManager.add(epic);
        SubTask subTask = getSubtask(epic);
        taskManager.add(subTask);

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(PATH);
        fileBackedTasksManager.repairFromFile(new File(PATH));

        Assertions.assertEquals(1, fileBackedTasksManager.getAllTasks().size());
        Assertions.assertEquals(1, fileBackedTasksManager.getAllEpics().size());
        Assertions.assertEquals(1, fileBackedTasksManager.getAllSubTasks().size());
    }

    @Test
    public void loadAndSaveEmptyTasks() {
        int taskId = taskManager.add((Task) null);
        int epicId = taskManager.add((Epic) null);
        int subTaskId = taskManager.add((SubTask) null);

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(PATH);
        fileBackedTasksManager.repairFromFile(new File(PATH));

        Assertions.assertEquals(0, taskId);
        Assertions.assertEquals(0, epicId);
        Assertions.assertEquals(0, subTaskId);

    }
}
