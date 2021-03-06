/*
Реализовать программу консольную оболочку Shell

1) Программа предоставляет набор команд. Команды вводятся с консоли. time, date, exit.
2) Каждая команда реализуется классом имплементирующим интерфейс Command.
3) Интерфейс должен описывать методы для получения имени и исполнения команды.

4) Для чтения с консоли пользуемся классом Scanner:
    Scanner in = new Scanner(System.in);
   nextLine(): считывает всю введенную строку

5) Для работы с датой/временем исследуем класс LocalDateTime:
    https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html

6) Интерфейс Command может определять вызов двух методов:
    getName() – возвращает имя команды.
    execute()  - выполнение команды.

На старте в программе инициализируется массив с набором команд, которые может исполнять программа Shell.
*/

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// лень делать кучу файлов, так что реализую все классы в одном пакете
interface Command {
    String getName();
    void execute();
}

abstract class CommandImpl implements Command {
    private String name;

    public CommandImpl(String name, boolean isTerminal) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void execute();
}

class CommandTime extends CommandImpl {

    public CommandTime() {
        super("time", false);
    }

    public void execute() {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}

class CommandDate extends CommandImpl {

    public CommandDate() {
        super("date", false);
    }

    public void execute() {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}

class CommandExit extends CommandImpl {

    public CommandExit() {
        super("exit", true);
    }

    public void execute() {
        System.exit(0);
    }
}

public class Lesson2 {

    private static Map<String, Command> commands;
    private static Scanner scanner;

    public static void main(String[] args) {

        // не понял как связать массив с набором команд с самими командами (не рефлексией надеюсь?), поэтому заменил его на мапу
        commands = Stream.of(new CommandTime(), new CommandDate(), new CommandExit()).collect(Collectors.toMap(c -> c.getName(), c -> c));

        scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Жду команду > ");
            String s = scanner.nextLine().toLowerCase().trim();

            if (commands.containsKey(s))
                commands.get(s).execute();
            else
                System.out.println("WTF?");
        }

    }

}