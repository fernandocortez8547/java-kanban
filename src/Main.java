import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();
        //создание простй задачи

            Task task = new Task(0, "Простая задача", "Описание", "new");
            taskManager.add(task);


        //Создание одного эпика с двумя подзадачами.Эпик:
            Epic epic = new Epic(0,
                "задача",
                "описание",
                "new");
            taskManager.add(epic);
            //Первая подзадача:
            SubTask subTask = new SubTask(0, "Подзадача - Первой задачи", "описание", "new", epic.getId());
            taskManager.add(subTask);
            epic.setSubIds(subTask.getId());
            //Вторая подзадача:
            SubTask subTask1 = new SubTask(0, "Подзадача - Первой задачи", "описание", "new", epic.getId());
            taskManager.add(subTask1);
            epic.setSubIds(subTask1.getId());

        //Создание одного эпика с одной подзадачей. Эпик:
            Epic epic1 = new Epic(0, "Вторая задача", "Описание", "new");
            taskManager.add(epic1);
            //Первая подзадача:
            SubTask subTask2 = new SubTask(0, "Подзадача - Второй задачи", "Описание", "new", epic1.getId());
            taskManager.add(subTask2);
            epic1.setSubIds(subTask2.getId());

        //Вывод на экран
        System.out.println(taskManager.returnEpics());
        System.out.println(taskManager.returnSubTasks(epic));
        System.out.println(taskManager.returnSubTasks(epic1));

    }

    static  void menu() {
        System.out.println("Что Вы хотите сделать:");
        System.out.println("1 - Добавить задачу");
        System.out.println("2 - Изменить статус задачи");
        System.out.println("3 - показать список задачи");
    }
}
