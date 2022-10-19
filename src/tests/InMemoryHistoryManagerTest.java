package tests;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;

public class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private int nextId = 0;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    private int idGeneration() {
        return ++nextId;
    }

    protected Task getTask() {
        return new Task(idGeneration(), "Test addNewTask", "Test addNewTask description",
                TaskStatus.NEW, LocalDateTime.now(), 60
        );
    }

    @Test
    public void addNewAndEmptyTaskToHistory() {
        int addResult = historyManager.add((Task) null);
        assertEquals(0, addResult, "Неправильное поведение при null.");
        assertEquals(0, historyManager.getHistory().size());

        Task task = getTask();
        addResult = historyManager.add(task);
        assertEquals(1, addResult, "Задача не добавляется.");
        assertEquals(1, historyManager.getHistory().size(), "Список не содержит задач.");

        historyManager.add(task);
        assertEquals(task, historyManager.getHistory().get(0));
        assertEquals(1, historyManager.getHistory().size(), "Добавляется дубликат задачи.");
    }

    @Test
    public void removeTaskFromHeadMidAndTail() {
        Task headTask = getTask();
        Task midTask = getTask();
        Task tailTask = getTask();

        historyManager.add(headTask);
        historyManager.add(midTask);
        historyManager.add(tailTask);

        historyManager.remove(midTask.getId());
        assertEquals(2, historyManager.getHistory().size());

        historyManager.remove(tailTask.getId());
        assertEquals(1, historyManager.getHistory().size());

        historyManager.remove(headTask.getId());
        assertEquals(0, historyManager.getHistory().size());

        int removeResult = historyManager.remove(-1);
        assertEquals(0, removeResult);
    }
}
