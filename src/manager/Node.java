package manager;

class Node<T> {
    T data;
    Node<T> next;
    Node<T> prev;

    public Node(Node<T> prev, T data, Node<T> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

}
