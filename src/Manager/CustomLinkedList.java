package Manager;
import Tasks.*;
import java.util.Map;
import java.util.HashMap;

public class CustomLinkedList {
    Node<Task> head;
    Node<Task> tail;
    int size = 0;
    Map<Integer, Node<Task>> idNode = new HashMap<>();

    public void linkLast(Task e) {
        if(contains(e.getId())) {
            remove(e.getId());
        }

        Node<Task> oldTail = tail;
        tail = new Node<Task>(oldTail, e, null);

        if(head == null)
            head = tail;
        else
            oldTail.next = tail;
        size++;

        idNode.put(tail.data.getId(), tail);
    }

    public Node<Task> getHead() {
        return head;
    }

    private boolean contains(int id) {
        return idNode.containsKey(id);
    }

    public int getSize() {
        return size;
    }

    public void remove(int id) {
        if(idNode.containsKey(id)) {
            Node<Task> currentNode = idNode.get(id);
            currentNode.prev.next = currentNode.next;
            currentNode.next.prev = currentNode.prev;
            idNode.remove(id);
        }
    }
}
    class Node<Task> {
        Task data;
        Node next;
        Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }


    }


