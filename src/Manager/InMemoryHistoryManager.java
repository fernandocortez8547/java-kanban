package Manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> historyList = new ArrayList<>();
    public static final int MAX_LIST_SIZE = 10;

    @Override
    public void add(Task task) {
        if(task.equals(null)) {
            historyList.add(task);
        }

        if (historyList.size() > MAX_LIST_SIZE) {
            historyList.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
