package manager;

public class Manager {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultTask() {
        return new HttpTaskManager("http://localhost:8078");
    }
}
