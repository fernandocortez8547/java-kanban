package tests;

import manager.FileBackedTasksManager;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.File;
public class FileBackedTasksManagerTest  extends TaskManagerTest{
    public static final String PATH = "tasks_test.csv";

    @Override
    public FileBackedTasksManager createManager() {
        taskManager = new FileBackedTasksManager(PATH);
        return (FileBackedTasksManager) taskManager;
    }
    @AfterEach
    public void afterEach() {
        File file = new File("PATH");
        file.delete();
    }

    @Test
    public void loadAndSaveTest() {
        Task task = getTask();
        Epic epic = getEpic();
        taskManager.add(task);
        taskManager.add(epic);
        SubTask subTask = getSubtask(epic);
        taskManager.add(subTask);

        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubTask(subTask.getId());

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File(PATH));

        assertEquals(1, fileBackedTasksManager.getAllTasks().size());
        assertEquals(1, fileBackedTasksManager.getAllEpics().size());
        assertEquals(1, fileBackedTasksManager.getAllSubTasks().size());

        assertEquals(3, fileBackedTasksManager.getHistory().size());
    }

    @Test
    public void loadAndSaveEmptyTasks() {
        int taskId = taskManager.add((Task) null);
        int epicId = taskManager.add((Epic) null);
        int subTaskId = taskManager.add((SubTask) null);

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File(PATH));

        assertNull(fileBackedTasksManager.getTask(taskId));
        assertNull(fileBackedTasksManager.getEpic(epicId));
        assertNull(fileBackedTasksManager.getSubTask(subTaskId));
    }

    @Test
    public void loadAndSaveTaskEmptyAndFullHistory() {
        Task task = getTask();
        Epic epic = getEpic();
        taskManager.add(task);
        taskManager.add(epic);
        SubTask subTask = getSubtask(epic);
        taskManager.add(subTask);

        assertEquals(0, taskManager.getHistory().size());

        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubTask(subTask.getId());

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("src/TestFile.csv"));

        assertEquals(3, fileBackedTasksManager.getHistory().size());
    }
}
