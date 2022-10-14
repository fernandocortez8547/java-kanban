package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import manager.TaskManager;
import tasks.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected Task getTask() {
        return new Task(0, "Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
    }

    protected Epic getEpic() {
        return new Epic(0, "Test addNewEpic", "Test addNewEpic description", TaskStatus.NEW);
    }

    protected SubTask getSubtask(Epic epic) {
        return new SubTask(0, "Test addNewSubtask", "Test addNewSubTask description", TaskStatus.NEW, epic.getId());
    }

    @Test
    public void addNewTask() {
        Task task = getTask();
        final int taskId = taskManager.add(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertEquals(TaskStatus.NEW, task.getStatus(), "Статус задачи неверный.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addNewEpic() {
        Epic epic = getEpic();
        final int epicId = taskManager.add(epic);
        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус эпика неверный.");

        final List<Epic> epics = taskManager.getAllEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");

        assertEquals(0, epic.getSubIds().size(), "Неверное количество подзадач");

        SubTask subTask = getSubtask(epic);
        taskManager.add(subTask);

        final List<SubTask> subTasks = taskManager.getEpicSubTasks(epicId);

        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
        assertEquals(subTask, subTasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    public void addNewSubTask() {
        Epic epic = getEpic();
        final int epicId = taskManager.add(epic);

        SubTask subTask = getSubtask(epic);
        final int subTaskId = taskManager.add(subTask);
        final SubTask savedSubTask = taskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");
        assertEquals(TaskStatus.NEW, subTask.getStatus(), "Статус подзадачи неверный.");

        List<SubTask> subTasks = taskManager.getAllSubTasks();

        assertNotNull(subTasks, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество эпиков.");
        assertEquals(subTask, subTasks.get(0), "Подзадачи не совпадают.");

        assertEquals(TaskStatus.NEW, subTask.getStatus(), "Статус подзадачи неверный.");
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус эпика неверный.");

        assertEquals(epicId, subTask.getEpicId(), "Эпики не совпадают.");
    }

    @Test
    public void addEmptyTask() {
        int taskId = taskManager.add((Task) null);
        assertEquals(0, taskId);
    }

    @Test
    public void addEmptyEpic() {
        int epicId = taskManager.add((Epic) null);
        assertEquals(0, epicId);
    }

    @Test
    public void addEmptySubTask() {
        int subTaskId = taskManager.add((SubTask) null);
        assertEquals(0, subTaskId);
    }

    @Test
    public void updateTaskStatusToInProgress() {
        Task task = getTask();
        taskManager.add(task);

        task.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.update(task);

        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus(), "Статус задачи не изменился.");
    }

    @Test
    public void updateEpicStatusToInProgress() {
        Epic epic = getEpic();
        taskManager.add(epic);

        epic.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.update(epic);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статус эпика не изменился.");
    }

    @Test
    public void updateSubTaskStatusToInProgress() {
        Epic epic = getEpic();
        taskManager.add(epic);

        SubTask subTask = getSubtask(epic);
        taskManager.add(subTask);

        subTask.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.update(subTask);

        assertEquals(TaskStatus.IN_PROGRESS, subTask.getStatus(), "Статус подзадачи не изменился.");
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статус эпика не изменился.");
    }

    @Test
    public void updateTaskStatusToDone() {
        Task task = getTask();
        taskManager.add(task);

        task.setStatus(TaskStatus.DONE);
        taskManager.update(task);

        assertEquals(TaskStatus.DONE, task.getStatus(), "Статус задачи не изменился.");
    }

    @Test
    public void updateEpicStatusToDone() {
        Epic epic = getEpic();
        taskManager.add(epic);

        epic.setStatus(TaskStatus.DONE);
        taskManager.update(epic);

        assertEquals(TaskStatus.DONE, epic.getStatus(), "Статус эпика не изменился.");
    }

    @Test
    public void updateSubTaskStatusToDone() {
        Epic epic = getEpic();
        taskManager.add(epic);

        SubTask subTask = getSubtask(epic);
        taskManager.add(subTask);

        subTask.setStatus(TaskStatus.DONE);
        taskManager.update(subTask);

        assertEquals(TaskStatus.DONE, subTask.getStatus(), "Статус подзадачи не изменился.");
        assertEquals(TaskStatus.DONE, epic.getStatus(), "Статус эпика не изменился.");
    }

    @Test
    public void updateTaskToUnavailableStatus() {
        int taskId = taskManager.update((Task) null);
        assertEquals(0, taskId);
    }

    @Test
    public void updateEpicToUnavailableStatus() {
        int epicId = taskManager.update((Epic) null);
        assertEquals(0, epicId);
    }

    @Test
    public void updateSubTaskToUnavailableStatus() {
        int subTaskId = taskManager.update((SubTask) null);
        assertEquals(0, subTaskId);
    }
    @Test
    public void deleteAllTasks() {
        Task task = getTask();
        taskManager.add(task);
        taskManager.clearingTasks();
        List<Task> allTasks = taskManager.getAllTasks();
        assertEquals(0, allTasks.size(), "Задачи не удаляются.");
    }

    @Test
    public void deleteAllEpics() {
        Epic epic = getEpic();

        taskManager.add(epic);
        taskManager.clearingEpics();

        List<Epic> allEpics = taskManager.getAllEpics();
        assertEquals(0, allEpics.size(), "Эпики не удаляются.");
    }

    @Test
    public void deleteAllSubTasksAndEpicSubTasks() {
        Epic epic = getEpic();
        taskManager.add(epic);
        SubTask subtask = getSubtask(epic);
        taskManager.add(subtask);
        taskManager.clearingSubTasks();

        List<SubTask> allSubTasks = taskManager.getAllSubTasks();
        assertEquals(0, allSubTasks.size(), "Подзадачи не удаляются.");

        taskManager.add(subtask);
        taskManager.clearingEpics();
        allSubTasks = taskManager.getAllSubTasks();
        assertEquals(0, allSubTasks.size(), "Подзадачи не удаляются.");

    }

    @Test
    public void deleteTaskById() {
        Task task = getTask();
        final int taskId = taskManager.add(task);
        taskManager.removeTask(taskId);
        assertNull(taskManager.getTask(taskId), "Задача не удаляется.");
    }

    @Test
    public void deleteEpicById() {
        Epic epic = getEpic();
        final int epicId = taskManager.add(epic);
        taskManager.removeEpic(epicId);
        assertNull(taskManager.getEpic(epicId), "Эпик не удаляется.");
    }

    @Test
    public void deleteSubTaskAndEpicSubTaskById() {
        Epic epic = getEpic();
        final int epicId = taskManager.add(epic);

        SubTask subTask = getSubtask(epic);
        final int subtaskId = taskManager.add(subTask);

        taskManager.removeSubTask(subtaskId);
        assertNull(taskManager.getSubTask(subtaskId), "Подзадача не удаляется");

        taskManager.add(subTask);
        taskManager.removeEpic(epicId);
        assertNull(taskManager.getSubTask(subtaskId), "Подзадача не удаляется.");
    }

    @Test
    public void deleteNonExistentIdTask() {
        int taskId = taskManager.removeTask(-1);
        assertEquals(0, taskId);
    }

    @Test
    public void deleteNonExistentIdEpic() {
        int taskId = taskManager.removeEpic(-1);
        assertEquals(0, taskId);
    }

    @Test
    public void deleteNonExistentIdSubTask() {
        int subTaskId = taskManager.removeSubTask(-1);
        assertEquals(0, subTaskId);
    }
}
