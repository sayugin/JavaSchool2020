import java.util.*;

/*
1. Создать лист из своих объектов (10-15 элементов в списке). Добавить, удалить и т.д.
2. Добавить дубли в список (1 - несколько раз один и тот же объект, 2 - дубль должен быть новым объектом с теми же параметрами, что уже имеет один из существующих в списке)
3. Вывести список элементов в читабельном виде.
4. Создать неповторяющееся упорядоченное множество с использованием компаратора и перенести значения из созданного листа.
5. Обход дерева с помощью forEach и iterator (подсчет или конкатинация из объектов коллекции используя условие, например "все начинаются с буквы", "больше какого-то значения")
6. Удалить третий элемент из множества.
7. Из существующей коллекции объектов создать ассоциативную карту, где ключ - объект, а значение - коллекция
8. Из существующей карты создать другую, в которой ключ остается прежним, а значение - вычисленное значение чего-либо из коллекции для ключа (по нескольким вариантам значений)
*/

class Book {
    private final String author;
    private final String title;

    public Book(String author, String title) {
        this.author = author;
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(author, book.author) && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, title);
    }

    @Override
    public String toString() {
        return this.author + " - " + this.title;
    }
}

public class Lesson4 {
    static Book[] arr = new Book[] {
            new Book("Bulgakov Mikhail","The Heart of a Dog"),
            new Book("Bulgakov Mikhail","The Master and Margarita"),
            new Book("Dostoevsky Fyodor","Crime and Punishment"),
            new Book("Doyle Arthur Conan","The Adventure of Sherlock Holmes"),
            new Book("Doyle Arthur Conan","The Hound of the Baskervilles"),
            new Book("Doyle Arthur Conan","The Poison Belt"),
            new Book("Henry O.","Heart of the West"),
            new Book("London Jack","The Call of the Wild"),
            new Book("London Jack","White Fang"),
            new Book("Orwell George","Animal Farm"),
            new Book("Orwell George","1984"),
            new Book("Shakespeare William","Hamlet, Prince of Denmark"),
            new Book("Shakespeare William","The Comedy of Errors"),
            new Book("Stevenson Robert Louis","Treasure Island"),
            new Book("Tolkien J.R.R.","The Fellowship of the Ring")
    };

    public static void main(String[] args) {
        System.out.println("***************** 1 - 3 ****************");
        @SuppressWarnings("unchecked") List<Book> list = new ArrayList(Arrays.asList(arr)); // свой список
        list.add(new Book("Wells Herbert","The Time Machine")); // добавил
        list.add(new Book("Wells Herbert","The Time Machine")); // добавил дубль
        list.set(list.size()-1, new Book("Wells Herbert","The War of the Worlds")); // заменил дубль
        list.add(new Book("Wells Herbert","The Time Machine")); // добавил дубль снова
        list.removeIf(b -> b.getAuthor().matches("D(.)+")); // удалил Достоевского с Конан Дойлем
        list.forEach(System.out::println); // вывел список

        System.out.println("***************** 4 ****************");
        // создал множество и перенес значения
        Set<Book> set = new TreeSet<>(Comparator.comparing(b -> b.getAuthor().concat(b.getTitle())));
        
        set.addAll(list);
        set.forEach(System.out::println);

        System.out.println("***************** 5 ****************");
        int count1 = 0;
        int count2 = 0;
        // обход через foreach и подсчет кол-ва книг с названием длиной до 15 символов
        for (Book b: set)
            if (b.getTitle().length() < 15)
                count1++;
        // обход через итератор и подсчет кол-ва книг с названием длиной свыше 15 символов
        Iterator<Book> iterator = set.iterator();
        while (iterator.hasNext())
            if (iterator.next().getTitle().length() >= 15)
                count2++;
        // результаты
        System.out.printf("total=%d, count1=%d, count2=%d%n", set.size(), count1, count2);

        System.out.println("***************** 6 ****************");
        // удаление 3-го элемента
        iterator = set.iterator();
        int i=0;
        while (iterator.hasNext() && i<3) {
            iterator.next();
            i++;
            if (i == 3)
                iterator.remove();
        }
        set.forEach(System.out::println);

        System.out.println("***************** 7 ****************");
        // создал мапу из сета
        Map<Book, Collection<Book>> map = new HashMap<>(set.size(), 0.9f);
        for (Book b: set)
            map.put(b, set);
        map.forEach((k,v) -> System.out.printf("%s: %s%n",k,v.toString()));

        System.out.println("***************** 8 ****************");
        // создал новую мапу с теми же ключами, в которой в качестве значений лежит кол-во других книг автора из первоначальной коллекции
        Map<Book, Long> newMap = new HashMap<>();
        for (Map.Entry<Book, Collection<Book>> e: map.entrySet()) {
            newMap.put(e.getKey(), e.getValue()
                    .stream()
                    .filter(b -> b.getAuthor().equals(e.getKey().getAuthor()) && b != e.getKey())
                    .count());
        }
        newMap.forEach((k,v) -> System.out.printf("%s: %s%n",k,v.toString()));
    }
}