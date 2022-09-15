package Manager;

import Tasks.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    public static void main(String[] args) {
        loadFromFile(new File("src" + File.separator + "TestFile.csv"));
        System.out.println(tasksList);
    }

    static String path;
    static List<String> tasksList = new ArrayList<>();

    public FileBackedTasksManager(String path) {
        this.path = path;
        try {
            take(path);
        } catch (ManagerSaveException ignored) {}
    }

    @Override
    public int add(Task task) {
        if(super.add(task) == 0) {
            return 0;
        }
        tasksList.add(FileToString.toString(task));
        try {
            save();
        } catch (ManagerSaveException ignored) {}
        return task.getId();
    }

    @Override
    public int add(Epic epic) {
        if(super.add(epic) == 0) {
            return 0;
        }
        tasksList.add(FileToString.toString(epic));
        try {
            save();
        } catch (ManagerSaveException ignored) {}
        return epic.getId();
    }

    @Override
    public int add(SubTask subtask) {
        if(super.add(subtask) == 0) {
            return 0;
        }
        tasksList.add(FileToString.toString(subtask));
        try {
            save();
        } catch (ManagerSaveException ignored) {}
        return subtask.getId();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {}
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {}
        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {}
        return subTask;
    }

//    @Override
//    public List<Task> getAllTasks() {
//
//    }

    public void save() throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(path, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic" + "\n");
            for (int i = 0; i < tasksList.size(); i++) {
                if(i == tasksList.size() - 1) {
                    writer.write(tasksList.get(i) + "\n");
                    break;
                }
                writer.write(tasksList.get(i) + ",\n");
            }
            writer.write("\n" + FileToString.historyToString(super.historyManager));
        } catch (IOException e) {
                throw new ManagerSaveException("Произошла ошибка сохранения.");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        return new FileBackedTasksManager(file.getPath());
    }



    public void take(String path) throws ManagerSaveException {
        try {
            String str = Files.readString(Paths.get(path));
        
        } catch (IOException e) {}
            //toString конвертирует не файл
//            String[] lines = fw.toString().split("\n");
//            for(int i = 1; i < lines.length; i++) {
//                add(FileToString.fromString(lines[i]));
//            }
//        } catch (IOException e) {
//            throw new ManagerSaveException("Произошла ошибка сохранения.");
//        }
    }
}
