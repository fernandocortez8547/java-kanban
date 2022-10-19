package util;

import manager.HistoryManager;
import tasks.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileConverter {

    private FileConverter() {
    }

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm:ss");

    public static String toString(Task task) {
        if(task instanceof SubTask) {
            SubTask subTask = (SubTask) task;
            return String.format(
                    "%d,%s,%s,%s,%s,%d,%s,%s,%s", subTask.getId(), typeConverter(subTask), subTask.getName(),
                    subTask.getStatus(), subTask.getDescription(), subTask.getEpicId(),
                    subTask.getStartTime().format(FORMATTER), subTask.getEndTime().format(FORMATTER), subTask.getDuration()
            );
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            return String.format(
                    "%d,%s,%s,%s,%s,%s,%s,%s", epic.getId(), typeConverter(epic), epic.getName(), epic.getStatus(),
                    epic.getDescription(), epic.getStartTime().format(FORMATTER),
                    epic.getEndTime().format(FORMATTER), epic.getDuration()
            );
        }

        return String.format(
                "%d,%s,%s,%s,%s,%s,%s,%s", task.getId(), typeConverter(task), task.getName(), task.getStatus(),
                task.getDescription(), task.getStartTime().format(FORMATTER),
                task.getEndTime().format(FORMATTER), task.getDuration()
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
                    Integer.parseInt(str[5]),
                    LocalDateTime.parse(str[6], FORMATTER),
                    Integer.parseInt(str[8])
            );
        } else if (str[1].equals("EPIC")) {
            return new Epic(Integer.parseInt(str[0]), str[2], str[4], statusConverter(str[3]),
                    LocalDateTime.parse(str[5], FORMATTER), Integer.parseInt(str[7])
            );
        }

        return new Task(Integer.parseInt(str[0]), str[2], str[4], statusConverter(str[3]),
                LocalDateTime.parse(str[5], FORMATTER), Integer.parseInt(str[7])
        );
    }

    public static List<Integer> historyFromString(String value) {
        String[] historyString = value.split(",");
        List<Integer> historyIds = new ArrayList<>();
        for (String s : historyString) {
            historyIds.add(Integer.parseInt(s.trim()));
        }
        return historyIds;
    }

    public static String historyToString(HistoryManager manager) {
        String historyString;

        if(manager.getIdsFromHistory().length != 0) {
            historyString = Arrays.toString(manager.getIdsFromHistory());
            return historyString.substring(1, historyString.length() - 1);
        }
        return "";
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
