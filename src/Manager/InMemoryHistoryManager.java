package Manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> historyList = new ArrayList<>();
    CustomLinkedList linkedList = new CustomLinkedList();


    @Override
    public void add(Task task) {
        if(task != null) {
            linkedList.linkLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        historyList.clear();
        Node<Task> taskNode = linkedList.getHead();

        while(taskNode != null) {
            historyList.add(taskNode.data);
            taskNode = taskNode.next;
        }

        return historyList;
    }

    @Override
    public void remove(int id) {
        linkedList.remove(id);
    }
}
