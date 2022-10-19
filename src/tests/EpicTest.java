package tests;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.TaskStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    //создал этот класс случайно
    //проверки всех статусов происходят через мэнэджер
    //по тз нашел только возможный без менеджера здесь тест пустого списка
    @Test
    public void emptySubTaskList() {
        Epic epic = new Epic(0, "Test addNewEpic", "Test addNewEpic description",
                TaskStatus.NEW, LocalDateTime.now(), 60
        );
        assertEquals(0, epic.getSubIds().size());
    }
}