package Manager;

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
