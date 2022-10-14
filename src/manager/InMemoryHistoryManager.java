package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    Node<Task> head;
    Node<Task> tail;
    Map<Integer, Node<Task>> idNode = new HashMap<>();


    @Override
    public int add(Task task) {
        if(task == null) {
            return 0;
        }

        if (idNode.containsKey(task.getId())) {
            remove(task.getId());
        }
        idNode.put(task.getId(), linkLast(task));

        return 1;
    }


    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        Node<Task> taskNode = head;

        while (taskNode != null) {
            historyList.add(taskNode.data);
            taskNode = taskNode.next;
        }

        return historyList;
    }

    @Override
    public int[] getIdsFromHistory() {
        int[] historyArray = new int[idNode.size()];
        Node<Task> taskNode = head;

        for (int i = 0; i < idNode.size(); i++) {
            historyArray[i] = taskNode.data.getId();
            taskNode = taskNode.next;
        }
        return historyArray;
    }


    @Override
    public int remove(int id) {
        if(idNode.get(id) == null) {
            return 0;
        }
        removeNode(idNode.get(id));
        idNode.remove(id);
        return id;
    }

    private Node<Task> linkLast(Task e) {

        Node<Task> oldTail = tail;
        tail = new Node<Task>(oldTail, e, null);

        if (head == null)
            head = tail;
        else
            oldTail.next = tail;


        return tail;
    }

    private void removeNode(Node<Task> taskNode) {

        if (taskNode.prev == null) {
            if (taskNode.next != null) {
                taskNode.next.prev = null;
                head = taskNode.next;
            } else {
                head = null;
                tail = null;
            }
        }

        if (taskNode.next == null) {
            if (taskNode.prev != null) {
                taskNode.prev.next = null;
                tail = taskNode.prev;
            } else {
                head = null;
                tail = null;
            }
        }

        if (taskNode.prev != null && taskNode.next != null) {
            taskNode.prev.next = taskNode.next;
            taskNode.next.prev = taskNode.prev;
        }

    }
}

