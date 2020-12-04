public class Lesson3 {
    public static void main(String[] args) {
        final int COUNT = 10_000_000;
        final int STEP = 2_000_000;

        for (int i = 0; i < COUNT / STEP; i++) {
            int elementCount = STEP * (i + 1);
            LList<Integer> list = new LList<>();
            long t1 = System.currentTimeMillis();
            for (int j = 0; j < elementCount; j++)
                list.add(j + 1);
            long t2 = System.currentTimeMillis();
            System.out.printf(String.format("Кол-во элементов: %d. Генерация списка: %d%n", elementCount, t2 - t1));

            t1 = System.currentTimeMillis();
            Integer data = list.get(elementCount - 2);
            t2 = System.currentTimeMillis();
            System.out.printf("Значение и время извлечения предпоследнего элемента: %d - %d%n", data, t2 - t1);
            System.out.println("*******************************************************************");
        }
    }
}

class Node<T> {

    T data;
    Node<T> next;

    public Node(T data) {
        this.data = data;
    }

    public boolean hasNext(){
        return next != null;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}

class LList<T> {


    private Node<T> first;
    private Node<T> last;
    private int size;

    public LList() {
        this.size = 0;
    }

    public void add(T item) {
        Node<T> newNode = new Node<>(item);
        if (this.last != null)
            this.last.next = newNode;
        else
            this.first = newNode;

        this.last = newNode;
        this.size++;

    }

    public T get(int index) {
        // проверяю что не выхожу за пределы списка
        // можно убрать проверку и получать первый или последний элемент списка, но это такое себе..
        if (index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException();

        // оптимизация для концов
        if (index == 0)
            return this.first.data;
        if (index == this.size - 1)
            return this.last.data;

        // разматываю с начала списка
        Node<T> currentNode = this.first;
        int i = 0;
        while (i < index && currentNode.hasNext()) {
            currentNode = currentNode.next;
            i++;
        }
        return currentNode.data;
    }

    public int size() {
        return this.size;
    }
}
