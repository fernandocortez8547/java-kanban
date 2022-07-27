import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();
        Epic epic = new Epic(0,
                "задача",
                "описание",
                "new");
        taskManager.add(epic);
        SubTask subTask = new SubTask(0,
                "название подзадачи",
                "описание",
                "new",
                epic.getId()
        );
        taskManager.add(subTask);
        taskManager.printAll();
    }

    static  void menu() {
        System.out.println("Что Вы хотите сделать:");
        System.out.println("1 - Добавить задачу");
        System.out.println("2 - Изменить статус задачи");
        System.out.println("3 - показать список задачи");
    }
}
