import Tasks.*;
import Manager.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Manager.getDefaultTask();

        //создание простой задачи

        Task task = new Task(0,
                "Простая задача",
                "Описание",
                TaskStatus.NEW);
        taskManager.add(task);

        //Создание одного эпика с двумя подзадачами.Эпик:

        Epic epic = new Epic(0,
                "задача",
                "описание",
                TaskStatus.NEW);
        taskManager.add(epic);
        //Первая подзадача:
        SubTask subTask = new SubTask(0,
                "Подзадача - Первой задачи",
                "описание",
                TaskStatus.NEW,
                epic.getId());
        taskManager.add(subTask);
        //Вторая подзадача:
        SubTask subTask1 = new SubTask(0,
                "Подзадача - Первой задачи",
                "описание",
                TaskStatus.NEW,
                epic.getId());
        taskManager.add(subTask1);

        //Создание одного эпика с одной подзадачей. Эпик:

        Epic epic1 = new Epic(0,
                "Вторая задача",
                "Описание",
                TaskStatus.NEW);
        taskManager.add(epic1);

        //Первая подзадача:

        SubTask subTask2 = new SubTask(0,
                "Подзадача - Второй задачи",
                "Описание",
                TaskStatus.DONE,
                epic1.getId());
        taskManager.add(subTask2);

        //Изменение имени эпика
        Epic epic2 = new Epic(0, "Задача 36546", "Описание", TaskStatus.NEW);
        taskManager.add(epic2);
        epic2.setName("Задача 3.1");
        epic2.setDescription("Описание 3");
        taskManager.update(epic2);

        //Изменение статуса подзадачи

        SubTask subTask3 = new SubTask(0, "Подзадача 1", "Описание", TaskStatus.NEW, epic2.getId());
        taskManager.add(subTask3);
        subTask3.setName("Подзадача 3 задачи");
        subTask3.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.update(subTask3);

        //Проверка истории

        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getEpic(epic2.getId());
        taskManager.getSubTask(subTask.getId());
        taskManager.getSubTask(subTask1.getId());
        taskManager.getSubTask(subTask2.getId());
        taskManager.getSubTask(subTask1.getId());
        taskManager.getSubTask(subTask2.getId());
        taskManager.getSubTask(subTask2.getId());
        taskManager.getSubTask(subTask2.getId());

        List<Task> history = taskManager.getHistory();
        for (int i = 0; i < history.size(); i++) {
            Task task100 = history.get(i);
            System.out.println((i+1) + ". " + task100);
        }
        Task task101 = new Task(15, "task101 1", "task101 description", TaskStatus.NEW);
        taskManager.add(task101);
        taskManager.getTask(task101.getId());
        history = taskManager.getHistory();
        System.out.println("New task task101 : ");

        for (int i = 0; i < history.size(); i++) {
            Task task100 = history.get(i);
            System.out.println((i+1) + ". " + task100);
        }

    }
}
