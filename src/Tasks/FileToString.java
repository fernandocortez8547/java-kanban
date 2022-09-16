package Tasks;

import Manager.HistoryManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileToString {

    private FileToString() {
    }

    public static String toString(Task task) {
        return String.format(
                "%s,%s,%s,%s,%s", task.getId(), typeConverter(task), task.getName(), task.getStatus(), task.getDescription()
        );
    }

    public static String toString(Epic epic) {
        return String.format(
                "%s,%s,%s,%s,%s", epic.getId(), typeConverter(epic), epic.getName(), epic.getStatus(), epic.getDescription()
        );
    }

    public static String toString(SubTask subTask) {
        return String.format(
                "%s,%s,%s,%s,%s,%s", subTask.getId(), typeConverter(subTask), subTask.getName(), subTask.getStatus(),
                subTask.getDescription(), subTask.getEpicId()
        );
    }

    public static Task fromString(String value) {
        String[] str = value.split(",");

        if (str[1].equals("SUBTASK")) {
            return new SubTask(
                    Integer.parseInt(str[0]),
                    str[2],
                    str[4],
                    statusConverter(str[3]),
                    Integer.parseInt(str[5])
            );
        } else if (str[1].equals("EPIC")) {
            return new Epic(Integer.parseInt(str[0]), str[2], str[4], statusConverter(str[3]));
        }

        return new Task(Integer.parseInt(str[0]), str[2], str[4], statusConverter(str[3]));
    }

    public static List<Integer> historyFromString(String value) {
        String[] historyString = value.split(",");
        List<Integer> historyIds = new ArrayList<>();
        for (int i = 0; i < historyString.length; i++) {
            historyIds.add(Integer.parseInt(historyString[i].trim()));
        }
        return historyIds;
    }

    public static String historyToString(HistoryManager manager) {
        String historyString = Arrays.toString(manager.getIdsFromHistory());
        return historyString.substring(1, (historyString.length() - 1));
    }

    private static TaskType typeConverter(Task task) {
        if (task instanceof SubTask) {
            return TaskType.SUBTASK;
        } else if (task instanceof Epic) {
            return TaskType.EPIC;
        }
        return TaskType.TASK;
    }

    private static TaskStatus statusConverter(String status) {
        if (status.equals("DONE")) {
            return TaskStatus.DONE;
        } else if (status.equals("IN_PROGRESS")) {
            return TaskStatus.IN_PROGRESS;
        }
        return TaskStatus.NEW;
    }
}
