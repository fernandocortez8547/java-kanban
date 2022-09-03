package Manager;

class Node<T> {
    T data;
    Node next;
    Node prev;

    public Node(Node prev, T data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}
