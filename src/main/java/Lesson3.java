public class Lesson3 {

}

class Node {
    public Node(Object data) {
        this.data = data;
    }
    Object data;
    Node next;
}

class LList {
    private Node root;

    public void add(Object item) {
        Node tmpItem = new Node(item);
        Node lastItem = findLast();

        if (lastItem != null) {
            lastItem.next = tmpItem;
        } else {
            root = tmpItem;
        }
    }

    public Object get(int id) {
        // TODO
        return null;
    }

    public int size() {
        int size = 0;

        if (root == null)
            return 0;
        else {
            size = 1;

            Node current = root;
            while (current.next != null) {
                size++;
                current = current.next;
            }
        }

        return size;
    }


    Node findLast() {
        if (root == null)
            return null;
        else {
            Node current = root;

            while (current.next != null) {
                current = current.next;
            }

            return current;
        }
    }
}
