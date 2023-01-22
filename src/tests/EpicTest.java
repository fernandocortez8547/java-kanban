package tests;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.TaskStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    public void emptySubTaskList() {
        Epic epic = new Epic(0, "Test addNewEpic", "Test addNewEpic description",
                TaskStatus.NEW, LocalDateTime.now(), 60
        );
        assertEquals(0, epic.getSubIds().size());
    }
}