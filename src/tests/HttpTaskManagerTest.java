package tests;

import localhost.KVServer;
import manager.HttpTaskManager;
import manager.Manager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    protected static TaskManager taskManager;
    protected static KVServer server;

    @Override
    public HttpTaskManager createManager() {
        try {
            server = new KVServer();
            server.start();
            taskManager = Manager.getDefaultTask();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return (HttpTaskManager) taskManager;
    }

    @AfterEach
    void stopServer(){
        server.stop();
    }

    @Test
    void loadTaskTest() {
        taskManager = createManager();
        Task task1 = new Task(0, "Task 1", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        Task task2 = new Task(0, "Task 2", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        int firstTaskId = taskManager.add(task1);
        int secondTaskId = taskManager.add(task2);

        taskManager.getTask(firstTaskId);
        taskManager.getTask(secondTaskId);

        System.out.println(taskManager.getHistory());

        assertEquals(2, taskManager.getAllTasks().size());
        assertEquals(2, taskManager.getHistory().size());
    }

    @Test
    void loadEpicTest() {
        Epic epic1 = new Epic(0, "Epic 1", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        Epic epic2 = new Epic(0, "Epic 2", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);
        int firstEpicId = taskManager.add(epic1);
        int secondEpicId = taskManager.add(epic2);

        taskManager.getEpic(firstEpicId);
        taskManager.getEpic(secondEpicId);

        assertEquals(2, taskManager.getAllEpics().size());
        assertEquals(2, taskManager.getHistory().size());
        assertEquals(taskManager.getAllEpics(), taskManager.getHistory());
    }

    @Test
    void loadSubTaskTest() {
        Epic epic1 = new Epic(0, "Epic 1", "Description",
                TaskStatus.NEW, LocalDateTime.now(), 60);

        int epicId = taskManager.add(epic1);

        SubTask subTask = new SubTask(0, "SubTask", "Description",
                TaskStatus.NEW, epicId, LocalDateTime.now(), 60);
        int subTaskId = taskManager.add(subTask);
        taskManager.getEpic(epicId);
        taskManager.getSubTask(subTaskId);

        assertEquals(1, taskManager.getAllSubTasks().size());
        assertEquals(2, taskManager.getHistory().size());
    }

}
