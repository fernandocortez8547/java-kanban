import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        //создание 1 эпика с двумя подзадачами
        Epic epic = new Epic(0,
                "задача",
                "описание",
                "new");
        taskManager.add(epic);
        SubTask subTask = new SubTask(0,
                "подзадача 1",
                "описание",
                "new",
                epic.getId()
        );
        taskManager.add(subTask);
        epic.setSubIds(subTask.getId());
        SubTask subTask1 = new SubTask(0,
                "подзадача 2",
                "описание",
                "new",
                epic.getId()
        );
        taskManager.add(subTask1);
        epic.setSubIds(subTask1.getId());

        taskManager.printAll();
        taskManager.printSubTasks(epic);

    }

    static  void menu() {
        System.out.println("Что Вы хотите сделать:");
        System.out.println("1 - Добавить задачу");
        System.out.println("2 - Изменить статус задачи");
        System.out.println("3 - показать список задачи");
    }
}
