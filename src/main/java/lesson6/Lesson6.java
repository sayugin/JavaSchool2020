package lesson6;

/*
Создать свой класс исключение, наследник Exception
Реализовать два поля: code, description, инициализация в конструкторе
code целочисленное, при создании инициализировать любое произвольное число
description строка, предназначено для описания бизнес ошибки, при создании инициализировать либым значением,
В конструкторе реализовать вызов родительского конструктора с аргументом message, т.е. у вас один конструктор с тремя параметрами
В коде из предыдущего задания, на ваше усмотрение сдалть проверку и бросать исключение
В качестве обработки исключения вывести в System.out code, description, message, после вызвать printStackTrace
В одном из методов бросать unchecked исключение, например IllegalArgumentException
Запустить приложение с заведомо ошибочным алгоритмом, т.е. что бы при вызове метода, который бросает ваше исключение,
было брошено и обработано, после вызов метода, который генерирует unchecked исключение, запустить программу
*/

import lesson5.*;

import java.util.*;
import java.util.stream.Collectors;

public class Lesson6 {

    public static void main(String[] args) {

        try {
            List<LogEntry> list = generateLog();
            list.forEach(System.out::println);

            // выкинул фильтрацию через классы, имплементирующие интерфейс и оставил только лямбды
            // как по мне так код вполне читаемый
            System.out.println("***** Last5 *****");
            Extractor<LogEntry> lastNExtractor = (l, n) -> l.stream().sorted(Comparator.comparing(LogEntry::getCreated).reversed()).limit(n).collect(Collectors.toList());
            lastNExtractor.filter(list, 5).forEach(System.out::println);

            System.out.println("***** First5 *****");
            Extractor<LogEntry> firstNExtractor = (l, n) -> l.stream().sorted(Comparator.comparing(LogEntry::getCreated)).limit(n).collect(Collectors.toList());
            firstNExtractor.filter(list, 5).forEach(System.out::println);

            System.out.println("***** Sales *****");
            Extractor<LogEntry> salesExtractor = (l, n) -> l.stream().filter(e -> e.getMessage().equals("Buy")).sorted(Comparator.comparing(LogEntry::getCreated).reversed()).limit(n).collect(Collectors.toList());
            salesExtractor.filter(list, 5).forEach(System.out::println);

        } catch (MyCheckedException e) {
            System.err.printf("(%d) - %s, %s%n", e.getCode(), e.getDescription(), e.getMessage());
            e.printStackTrace();
            throw new MyUncheckedException();
        }
    }

    private static List<LogEntry> generateLog() throws MyCheckedException {

        long start = System.currentTimeMillis();

        List<LogEntry> list = new ArrayList<>();
        list.add(new LogEntry("Start server"));
        list.add(new LogEntry("Wait"));
        list.add(new LogEntry("Wait"));
        list.add(new LogEntry("Wait"));
        list.add(new LogEntry("Wait"));
        list.add(new LogEntry("Wait"));
        list.add(new LogEntry("Wait"));

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

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {/*NOP*/}

        list.add(new LogEntry("Stop server"));

        if (System.currentTimeMillis()-start > 8500)
            throw new MyCheckedException(666, "Too late", "so long");

        return list;
    }


}
