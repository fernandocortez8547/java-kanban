package Manager;

import Tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    public static void main(String[] args) {
        TaskManager manager = loadFromFile(new File("src" + File.separator + "TestFile.csv"));
        //можно переделать вывод под String.format
        System.out.println("Таски: \n" + manager);
        System.out.println("История: \n" + manager.getHistory());
    }

    String path;
    static List<String> tasksStringList = new ArrayList<>();
    //нужно доб-ть булево значение для 40 строки add,
    // в случае, если файл восстанавливается не надо сохранять его еще раз

    //разобраться почему true выдаёт ошибку, нужно наоборот
    static boolean isLoaded = true;

    public FileBackedTasksManager(String path) {
        this.path = path;
    }

    private FileBackedTasksManager(File file) {
        try {
            take(file);
        } catch (ManagerSaveException ignored) {
        }
    }

    @Override
    public int add(Task task) {
        if (super.add(task) == 0) {
            return 0;
        }
        tasksStringList.add(FileToString.toString(task));

        if (isLoaded) {
            try {
                save();
            } catch (ManagerSaveException ignored) {
            }
        }
        return task.getId();
    }

    @Override
    public int add(Epic epic) {
        if (super.add(epic) == 0) {
            return 0;
        }
        tasksStringList.add(FileToString.toString(epic));

        if (isLoaded) {
            try {
                save();
            } catch (ManagerSaveException ignored) {
            }
        }
        return epic.getId();
    }

    @Override
    public int add(SubTask subtask) {
        if (super.add(subtask) == 0) {
            return 0;
        }
        tasksStringList.add(FileToString.toString(subtask));

        if (isLoaded) {
            try {
                save();
            } catch (ManagerSaveException ignored) {
            }
        }
        return subtask.getId();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        if (isLoaded) {
            try {
                save();
            } catch (ManagerSaveException ignored) {
            }
        }
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        if (isLoaded) {
            try {
                save();
            } catch (ManagerSaveException ignored) {
            }
        }

        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        if(isLoaded) {
            try {
                save();
            } catch (ManagerSaveException ignored) {
            }
        }
        return subTask;
    }

    @Override
    public List<Task> getAllTasks() {
        return super.getAllTasks();
    }

    public void save() throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(path, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic" + "\n");
            for (int i = 0; i < tasksStringList.size(); i++) {
                if (i == tasksStringList.size() - 1) {
                    writer.write(tasksStringList.get(i) + "\n");
                    break;
                }
                writer.write(tasksStringList.get(i) + ",\n");
            }
            writer.write("\n" + FileToString.historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка сохранения.");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        isLoaded = false;
        return new FileBackedTasksManager(file);
    }


    public void take(File file) throws ManagerSaveException {

        try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
            List<String> lines = new ArrayList<>();
            while (bf.ready()) {
                lines.add(bf.readLine());
            }

            for (int line = 1; line <= (lines.size() - 1); line++) {

                if (lines.get(line).isEmpty()) {
                    final int nextLine = line + 1;
                    if (nextLine != lines.size() && !(lines.get(nextLine).isEmpty()) ) {
                        for(int id : FileToString.historyFromString(lines.get(nextLine))) {
                            repairHistory(id);
                        }
                        break;
                    }
                }

                Task task = FileToString.fromString(lines.get(line));
                if (task instanceof SubTask) {
                    //возможно getSubtask не нужен
                    //достаточно привдения
                    add(getSubtask((SubTask) task));
                } else if (task instanceof Epic) {
                    add(getEpic((Epic) task));
                } else
                    add(task);
            }


        } catch (IOException ignored) {
            throw new ManagerSaveException("Произошла ошибка чтения из файла.");
        }
    }

    private void repairHistory(int id) {
        if(subTasks.containsKey(id))
            historyManager.add(subTasks.get(id));
        else if (epics.containsKey(id))
            historyManager.add(epics.get(id));
        else if(tasks.containsKey(id))
            historyManager.add(tasks.get(id));
    }

    private Epic getEpic(Epic epic) {
        return new Epic(epic.getId(), epic.getName(), epic.getDescription(), epic.getStatus());
    }

    private SubTask getSubtask(SubTask subTask) {
        return new SubTask(subTask.getId(), subTask.getName(), subTask.getDescription(), subTask.getStatus(), subTask.getEpicId());
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
