package lesson5;
/*
1. Создать класс Client, у которого будет поле имя. Создать класса Client, в котором должны быть все поля и свойства Client,
а так же реализован идентификатор клиента. На основе класса с идентификатором создать потомка с реализацией сколько потратил.
В отдельном классе создать метод, которые будут принимать коллекции, элементами которой могут быть классы, которые содержат
данные о клиенте (т.е. как минимум предыдущие три класса).  Создать метод, который принимает коллекцию, элементы которой могут
быть только Client и созданный ранее класс с идентификатором.
2. Создать интерфейс или класс с обобщенным(и) типом(типами) - имя придумать самостоятельно.
3. Добавить методы в интерфейс. Например, max, sort, middle и т.д., которые будут принимать коллекцию с обобщенным типом интерфейса..
4. Имплементировать интерфейс - создать несколько классов, которые реализуют методы интерфейса. Один ищет клиента с максимальным
количеством букв в имени, и сортирует в выбранном вами порядке по имени и т.д. Другой ищет, кто больше всех потратил, сортирует,
ищет среднее потраченное всеми клиентами и т.д. В отдельном классе вызвать реализованные методы с выводом результата.
5. Переписать код из предыдущего урока, так что бы все реализации вычислений - среднего, максимального и т.д. были реализованы
в обобщенных классах. Должен быть единый интерфейс, но при этом, для каждого вычисления отдельный класс.
В отдельном классе вызвать реализованные методы с выводом результата.

Не обязательно создавать именно класс Client, и не обязательно именно методы max, sort, middle. Если будет своя тематика объектов - приветствуется.
*/
import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.Collectors;

public class Lesson5 {

    public static void main(String[] args) throws InterruptedException { // понимаю что нехорошо выбрасывать исключение из мэйна, но тут пусть будет
        List<LogEntry> list = new ArrayList<>();
        //List<UserActivity> list = new ArrayList<>();
        list.add(new LogEntry("Start server"));
        list.add(new LogEntry("Wait"));
        list.add(new LogEntry("Wait"));
        list.add(new LogEntry("Wait"));
        list.add(new LogEntry("Wait"));
        list.add(new LogEntry("Wait"));
        list.add(new LogEntry("Wait"));

        // формирую логи в нескольких потоках чтобы было нескучно
        Thread t1 = new Thread((() -> {
            String login = "JBond";
            list.add(new UserActivity("Log on", login));
            list.add(new UADetails("Click", login, "somewhere"));
            list.add(new UADetails("Buy", login, "something good"));
            list.add(new UADetails("Click", login, ", simply click"));
            list.add(new UADetails("Buy", login, "something else"));
            list.add(new UserActivity("Log out", login));
        }));
        t1.start();

        Thread t2 = new Thread((() -> {
            String login = "MStirlitz";
            list.add(new UserActivity("Log on", login));
            list.add(new UADetails("Click", login, "somewhere"));
            list.add(new UADetails("Comment", login, "something"));
            list.add(new UADetails("Click", login, "somewhere"));
            list.add(new UserActivity("Log out", login));
        }));
        t2.start();

        Thread t3 = new Thread((() -> {
            String login = "EHunt";
            list.add(new UserActivity("Log on", login));
            list.add(new UADetails("Click", login, "1"));
            list.add(new UADetails("Click", login, "2"));
            list.add(new UADetails("Click", login, "3"));
            list.add(new UADetails("Click", login, "4"));
            list.add(new UADetails("Comment", login, "4"));
            list.add(new UserActivity("Log out", login));
        }));
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        list.add(new LogEntry("Stop server"));

        // смотрю что получилось
        list.forEach(System.out::println);

        // получаю и вывожу топ-5 сообщений
        // событие Wait попадет в вывод в случае если на вход функции подать коллекцию LogEntry и не попадет в случае UserActivity
        System.out.println("***** Топ-5 *****");
        Map<String, Long> map = aggregate(list)
                .entrySet()
                .stream()
                .sorted((e1,e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .collect(toMap(Map.Entry::getKey , Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
        map.forEach((k,v) -> System.out.printf("%s - %d%n", k, v));

        // для тренировки я конечно сделал пп.3-5 и создал классы, имплементирующие интерфейс из п.2, но на мой взгляд
        // вариант с передачей функции прямо в интерфейс будет и проще, и гибче
        System.out.println("***** Last5 *****");
        List<LogEntry> last5List = new LastNExtractor<>().filter(list, 5);
        last5List.forEach(System.out::println);

        System.out.println("***** Last5a *****");
        Extractor<LogEntry> extractor = (l,n) -> l.stream().sorted(Comparator.comparing(LogEntry::getCreated).reversed()).limit(n).collect(Collectors.toList());
        extractor.filter(list,5).forEach(System.out::println);

        System.out.println("***** First5 *****");
        List<LogEntry> first5List = new FirstNExtractor<>().filter(list, 5);
        first5List.forEach(System.out::println);

        System.out.println("***** First5a *****");
        extractor = (l,n) -> l.stream().sorted(Comparator.comparing(LogEntry::getCreated)).limit(n).collect(Collectors.toList());
        extractor.filter(list,5).forEach(System.out::println);

        System.out.println("***** Sales *****");
        List<LogEntry> salesList = new SalesExtractor<>().filter(list, 5);
        salesList.forEach(System.out::println);

        System.out.println("***** SalesA *****");
        extractor = (l,n) -> l.stream().filter(e -> e.getMessage().equals("Buy")).sorted(Comparator.comparing(LogEntry::getCreated).reversed()).limit(n).collect(Collectors.toList());
        extractor.filter(list,5).forEach(System.out::println);



    }

    private static Map<String, Long> aggregate(List<? extends LogEntry> list) {
        return list.stream().collect(groupingBy(LogEntry::getMessage, counting()));
    }
}
