package manager;

import tasks.*;
import util.FileConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    protected static String PATH = "src/TestFile.csv";
    protected File file = new File(PATH);
    static List<String> tasksStringList = new ArrayList<>();

    public FileBackedTasksManager() {
        load(file);
    }
    public FileBackedTasksManager(File file) {
        this.file = file;
        repairFromFile(file);
        PATH = file.getPath();
    }

    @Override
    public int add(Task task) {
        if (super.add(task) == 0) {
            return 0;
        }

        tasksStringList.add(FileConverter.toString(task));
        save();

        return task.getId();
    }

    @Override
    public int add(Epic epic) {
        if (super.add(epic) == 0) {
            return 0;
        }

        tasksStringList.add(FileConverter.toString(epic));
        save();

        return epic.getId();
    }

    @Override
    public int add(SubTask subtask) {
        if (super.add(subtask) == 0) {
            return 0;
        }

        tasksStringList.add(FileConverter.toString(subtask));
        save();

        return subtask.getId();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();

        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();

        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        save();

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
    public int removeTask(int taskId) {
        if(!tasks.containsKey(taskId)) {
            return 0;
        }

        tasksStringList.remove(FileConverter.toString(tasks.get(taskId)));
            int supTaskId = super.removeTask(taskId);
            save();

        return supTaskId;
    }

    @Override
    public int removeEpic(int epicId) {
            if(!epics.containsKey(epicId)) {
                return 0;
            }
            Epic epic = epics.get(epicId);
            for (int id : epic.getSubIds()) {
                tasksStringList.remove(FileConverter.toString(subTasks.get(id)));
            }
            tasksStringList.remove(FileConverter.toString(epics.get(epicId)));

            int supEpicId = super.removeEpic(epicId);

            save();


            return supEpicId;
    }

    @Override
    public int removeSubTask(int subTaskId) {
        if(!subTasks.containsKey(subTaskId)) {
            return 0;
        }

            tasksStringList.remove(FileConverter.toString(subTasks.get(subTaskId)));
            int supSubTaskId = super.removeSubTask(subTaskId);
            try {
                save();
            } catch (ManagerSaveException e) {
                System.out.println(e.getMessage() + Arrays.toString(e.getStackTrace()));
            }

            return supSubTaskId;
    }

    @Override
    public List<Task> getAllTasks() {
        return super.getAllTasks();
    }

    public void save() throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(PATH, StandardCharsets.UTF_8)) {
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
            throw new ManagerSaveException("Ошибка сохранения");
        }
    }

    public static FileBackedTasksManager load(File file) {
        return new FileBackedTasksManager(file);
    }


    public void repairFromFile(File file) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
            while (bf.ready()) {
                lines.add(bf.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException("Ошибка записи");
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
    }

    private void repairManagerFields(Task task) {
        if (task instanceof SubTask) {
            subTasks.put(task.getId(), (SubTask) task);
            Epic epic = epics.get(((SubTask) task).getEpicId());
            epic.addSubTaskId(epic.getId());
        } else if (task instanceof Epic)
            epics.put(task.getId(), (Epic) task);
        else
            tasks.put(task.getId(), task);
    }

    protected void repairHistory(int id) {
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
