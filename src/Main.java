import Tasks.*;
import Manager.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();
        //создание простой задачи
        Task task = new Task(0,
                "Простая задача",
                "Описание",
                TaskStatus.NEW);
        taskManager.add(task);
        System.out.println(taskManager.getTask(50));
        System.out.println(taskManager.getTask(task.getId()) + "\n");
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

        System.out.println(taskManager.getEpic(epic.getId()));
        System.out.println(taskManager.getEpicSubTasks(epic.getId()) + "\n");

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
        System.out.println(taskManager.getEpic(epic1.getId()));
        System.out.println(taskManager.getEpicSubTasks(epic1.getId()) + "\n");


        //Изменение имени эпика
        Epic epic2 = new Epic(0, "Задача 36546", "Описание", TaskStatus.NEW);
        taskManager.add(epic2);
        System.out.println(taskManager.getEpic(epic2.getId()));
        epic2.setName("Задача 3.1");
        epic2.setDescription("Описание 3");
        taskManager.update(epic2);
        System.out.println(taskManager.getEpic(epic2.getId()));

        //Изменение статуса подзадачи
        SubTask subTask3 = new SubTask(0, "Подзадача 1", "Описание", TaskStatus.NEW, epic2.getId());
        taskManager.add(subTask3);
        System.out.println(taskManager.getSubTask(subTask3.getId()));
        subTask3.setName("Подзадача 3 задачи");
        subTask3.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.update(subTask3);
        System.out.println(taskManager.getSubTask(subTask3.getId()));
        System.out.println(taskManager.getEpic(epic2.getId()));
        taskManager.deleteSubTask(subTask3.getId());
        System.out.println(taskManager.getEpicSubTasks(epic2.getId()));

        System.out.println("\n" + taskManager.getAllEpics());

        List<Task> history =taskManager.getHistory();
        for(int i = 0; i<history.size(); i++) {
            Task task100 = history.get(i);
            System.out.println(i + ". " + task100);
        }
        Task govno = new Task(15, "govno 1", "govno description", TaskStatus.NEW);
        taskManager.add(govno);
        taskManager.getTask(govno.getId());
        history = taskManager.getHistory();
        System.out.println("New task govno : " );
        for(int i = 0; i<history.size(); i++) {
            Task task100 = history.get(i);
            System.out.println(i + ". " + task100);
        }
    }
}
