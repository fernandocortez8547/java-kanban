import java.util.Scanner;
import Tasks.*;
import Manager.*;
public class Main {
    public static void main(String[] args) {
        //Наставник сказал, что проверки неправильного ввода не предусмотрено этой программой, потому что мы в них утоним ¯\_(ツ)_/¯
        TaskManager taskManager = new TaskManager();
        //создание простой задачи
        Task task = new Task(0,
                "Простая задача",
                "Описание",
                "new");
        taskManager.add(task);

        System.out.println(taskManager.returnTask(task.getId()) + "\n");
        //Создание одного эпика с двумя подзадачами.Эпик:
        Epic epic = new Epic(0,
                "задача",
                "описание",
                "new");
        taskManager.add(epic);
        //Первая подзадача:
        SubTask subTask = new SubTask(0,
                "Подзадача - Первой задачи",
                "описание",
                "new",
                epic.getId());
        taskManager.add(subTask);
        //Вторая подзадача:
        SubTask subTask1 = new SubTask(0,
                "Подзадача - Первой задачи",
                "описание",
                "inProgress",
                epic.getId());
        taskManager.add(subTask1);

        System.out.println(taskManager.returnEpic(epic.getId()));
        System.out.println(taskManager.returnEpicSubTasks(epic.getId()) + "\n");

        //Создание одного эпика с одной подзадачей. Эпик:
        Epic epic1 = new Epic(0,
                "Вторая задача",
                "Описание",
                "new");
        taskManager.add(epic1);
        //Первая подзадача:
        SubTask subTask2 = new SubTask(0,
                "Подзадача - Второй задачи",
                "Описание",
                "done",
                epic1.getId());
        taskManager.add(subTask2);
        System.out.println(taskManager.returnEpic(epic1.getId()));
        System.out.println(taskManager.returnEpicSubTasks(epic1.getId()) + "\n");


        //Изменение имени эпика
        Epic epic2 = new Epic(0, "Задача 3", "Описание", "new");
        taskManager.add(epic2);
        System.out.println(taskManager.returnEpic(epic2.getId()));
        epic2.setName("Задача 3.1");
        taskManager.updateEpicName(epic2);
        System.out.println(taskManager.returnEpic(epic2.getId()));

        //Изменение имени подзадачи
        SubTask subTask3 = new SubTask(0, "Подзадача 1", "Описание", "new", epic2.getId());
        taskManager.add(subTask3);
        System.out.println(taskManager.returnSubTask(subTask3.getId()));
        subTask3.setName("Подзадача 3 задачи");
        taskManager.update(subTask3);
        System.out.println(taskManager.returnSubTask(subTask3.getId()));
        System.out.println(taskManager.returnEpic(epic2.getId()));

        System.out.println("\n" + taskManager.returnAllEpics());
    }
}
