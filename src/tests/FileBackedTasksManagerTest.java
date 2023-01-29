package tests;

import manager.FileBackedTasksManager;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.*;

public class FileBackedTasksManagerTest  extends TaskManagerTest{
    public static final String PATH = "src/tests/tasks_test.csv";
    public final File file = new File(PATH);

    @AfterEach
    public void clearingManagerFields() {
        if(!taskManager.getAllTasks().isEmpty()) {
            taskManager.clearingTasks();
        }
        if(!taskManager.getAllEpics().isEmpty()) {
            taskManager.clearingEpics();
        }
    }
    @Override
    public FileBackedTasksManager createManager() {
        taskManager = new FileBackedTasksManager(file);
        return (FileBackedTasksManager) taskManager;
    }
    @AfterEach
    public void afterEach() {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(" ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        file.delete();
    }

    @Test
    public void loadAndSaveTest() {
        taskManager.clearingTasks();

        Task task = getTask();
        Epic epic = getEpic();
        taskManager.add(task);
        taskManager.add(epic);
        SubTask subTask = getSubtask(epic);
        taskManager.add(subTask);

        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubTask(subTask.getId());

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.load(new File(PATH));

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

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.load(new File(PATH));

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

//        assertEquals(0, taskManager.getHistory().size());

        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubTask(subTask.getId());

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager
                .load(new File("src/tests/tasks_test.csv"));

        assertEquals(3, fileBackedTasksManager.getHistory().size());
    }
}
