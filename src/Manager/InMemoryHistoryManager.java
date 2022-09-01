package Manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    Node<Task> head;
    Node<Task> tail;
    Map<Integer, Node<Task>> idNode = new HashMap<>();
    private List<Task> historyList = new ArrayList<>();

    int size = 0;

    @Override
    public void add(Task task) {

        if(idNode.containsKey(task.getId())) {
            remove(task.getId());
        }

        if(task != null) {
            idNode.put(task.getId(),linkLast(task));
        }
    }

    @Override
    public List<Task> getHistory() {
        historyList.clear();
        Node<Task> taskNode = head;

        while(taskNode != null) {
            historyList.add(taskNode.data);
            taskNode = taskNode.next;
        }

        return historyList;
    }

    @Override
    public void remove(int id) {
        removeNode(idNode.get(id));
    }

    //Дальше идёт реализация двусвязного списка

    private Node<Task> linkLast(Task e) {

        Node<Task> oldTail = tail;
        tail = new Node<Task>(oldTail, e, null);

        if(head == null)
            head = tail;
        else
            oldTail.next = tail;
        size++;

        return tail;
    }

    private void removeNode(Node<Task> taskNode) {

            if(taskNode.prev == null) {
                if(taskNode.next != null) {
                    taskNode.next.prev = null;
                    head = taskNode.next;
                    size--;
                } else {
                    head = null;
                    tail = null;
                    size = 0;
                }
            }

            if (taskNode.next == null) {
                if(taskNode.prev != null) {
                    taskNode.prev.next = null;
                    tail = taskNode.prev;
                } else {
                    head = null;
                    tail = null;
                    size = 0;
                }
            }

            if(taskNode.prev != null && taskNode.next != null) {
                taskNode.prev.next = taskNode.next;
                taskNode.next.prev = taskNode.prev;
            }
        }
}

