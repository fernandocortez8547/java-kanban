import Tasks.*;
import Manager.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic(0, "Epic", "Epic Description", TaskStatus.NEW);

        taskManager.add(epic);
        taskManager.getEpic(epic.getId());

        SubTask subTask1 = new SubTask(0, "Subtask1", "Subtask1 description", TaskStatus.NEW, epic.getId());
        SubTask subTask2 = new SubTask(0, "Subtask2", "Subtask2 description", TaskStatus.NEW, epic.getId());
        SubTask subTask3 = new SubTask(0, "Subtask3", "Subtask3 description", TaskStatus.NEW, epic.getId());

        Epic epic1 = new Epic(0, "Epic1", "Epic1 Description", TaskStatus.NEW);

        taskManager.add(subTask1);
        taskManager.add(subTask2);
        taskManager.add(epic1);
        taskManager.add(subTask3);

        taskManager.getSubTask(subTask2.getId());
        taskManager.getSubTask(subTask3.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getSubTask(subTask1.getId());
        taskManager.getEpic(epic1.getId());

        List<Task> list = taskManager.getHistory();
        System.out.println(list);

        taskManager.removeEpic(epic.getId());
        list = taskManager.getHistory();
        System.out.println(list);
    }
}
