package Manager;
import java.util.Map;
import java.util.HashMap;

public class CustomLinkedList<E> {
    Node<E> head;
    Node<E> tail;
    int size = 0;
    Map<Integer, CustomLinkedList<E>> linkedId;

    public CustomLinkedList() {
        linkedId = new HashMap<>();
        head = new Node<E>(null, null, tail);
        tail = new Node(head, null, null);
    }

    public void addFirst(E e) {
        Node<E> oldHead = head;
        head = new Node<E>( null, e, oldHead);
        if(oldHead == null)
            tail = head;
        else
            oldHead.prev = head;
        size++;
    }

    public void addLast(E e) {
        Node<E> oldTail = tail;
        head = new Node<E>( tail, e, null);
        if(head == null)
            head = oldTail;
        else
            oldTail.next = head;
        size++;
    }



    }
    class Node<E> {
        E data;
        Node next;
        Node prev;

        public Node(Node prev, E data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }


    }


