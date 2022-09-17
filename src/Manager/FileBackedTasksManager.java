package Manager;

import Tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    public static void main(String[] args) {
        TaskManager manager = loadFromFile(new File("src" + File.separator + "TestFile.csv"));
        //можно переделать восстан-е под String.format
        System.out.println("Таски: \n" + manager);
        System.out.println("История: \n" + manager.getHistory());
    }

    String path;
    static List<String> tasksStringList = new ArrayList<>();
    static boolean isLoaded;

    public FileBackedTasksManager(String path) {
        isLoaded = true;
        this.path = path;
    }

    private FileBackedTasksManager(File file) {
        repairFromFile(file);
        isLoaded = true;
    }

    @Override
    public int add(Task task) {
        if (super.add(task) == 0) {
            return 0;
        }
        tasksStringList.add(FileConverter.toString(task));

        if (isLoaded) {
            save();
        }
        return task.getId();
    }

    @Override
    public int add(Epic epic) {
        if (super.add(epic) == 0) {
            return 0;
        }
        tasksStringList.add(FileConverter.toString(epic));

        if (isLoaded) {
            save();
        }
        return epic.getId();
    }

    @Override
    public int add(SubTask subtask) {
        if (super.add(subtask) == 0) {
            return 0;
        }
        tasksStringList.add(FileConverter.toString(subtask));

        if (isLoaded) {
            save();
        }
        return subtask.getId();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        if (isLoaded) {
            save();
        }
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        if (isLoaded) {
            save();
        }

        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        if (isLoaded) {
            save();
        }
        return subTask;
    }

    @Override
    public void clearingTasks() {
        super.clearingTasks();
        save();
    }

    @Override
    public void clearingEpics() {
        super.clearingEpics();
        save();
    }

    @Override
    public void clearingSubTasks() {
        super.clearingSubTasks();
        save();
    }

    @Override
    public void removeTask(int taskId) {
        tasksStringList.remove(FileConverter.toString(tasks.get(taskId)));
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeEpic(int epicId) {
        Epic epic = epics.get(epicId);
        for (int id : epic.getSubIds()) {
            tasksStringList.remove(FileConverter.toString(subTasks.get(id)));
        }
        tasksStringList.remove(FileConverter.toString(epics.get(epicId)));

        super.removeEpic(epicId);

        save();
    }

    @Override
    public void removeSubTask(int subTaskId) {
        tasksStringList.remove(FileConverter.toString(subTasks.get(subTaskId)));
        super.removeSubTask(subTaskId);

        save();
    }

    @Override
    public List<Task> getAllTasks() {
        return super.getAllTasks();
    }

    public void save() {
        try (FileWriter writer = new FileWriter(path, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic" + "\n");
            for (int i = 0; i < tasksStringList.size(); i++) {
                if (i == tasksStringList.size() - 1) {
                    writer.write(tasksStringList.get(i) + "\n");
                    break;
                }
                writer.write(tasksStringList.get(i) + ",\n");
            }
            String historyStr = FileConverter.historyToString(historyManager);

            if (historyStr.isEmpty()) {
                writer.close();
            } else
                writer.write("\n" + historyStr);
        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Ошибка сохранения");
            } catch (ManagerSaveException saveException) {
                System.out.println(saveException.getMessage() + Arrays.toString(e.getStackTrace()));
            }
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        isLoaded = false;
        return new FileBackedTasksManager(file);
    }


    public void repairFromFile(File file){
        try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
            List<String> lines = new ArrayList<>();
            while (bf.ready()) {
                lines.add(bf.readLine());
            }

            for (int line = 1; line <= (lines.size() - 1); line++) {

                if (lines.get(line).isEmpty()) {
                    final int nextLine = (line + 1);
                    if (nextLine != lines.size() && !(lines.get(nextLine).isEmpty())) {
                        for (int id : FileConverter.historyFromString(lines.get(nextLine))) {
                            repairHistory(id);
                        }
                        break;
                    }
                }

                repairManagerFields(FileConverter.fromString(lines.get(line)));
            }
        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Ошибка записи");
            } catch (ManagerSaveException saveException) {
                System.out.println(saveException.getMessage() + Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private void repairManagerFields(Task task) {
        if (task instanceof SubTask)
            add((SubTask) task);
        else if (task instanceof Epic)
            add((Epic) task);
        else
            add(task);
    }

    private void repairHistory(int id) {
        if (subTasks.containsKey(id))
            historyManager.add(subTasks.get(id));
        else if (epics.containsKey(id))
            historyManager.add(epics.get(id));
        else if (tasks.containsKey(id))
            historyManager.add(tasks.get(id));
    }

    @Override
    public String toString() {
        return "FileBackedTasksManager{" +
                "epics=" + epics +
                ", subTasks=" + subTasks +
                ", tasks=" + tasks +
                '}';
    }
}
