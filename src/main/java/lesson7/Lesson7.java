package lesson7;

/*
Написать консольное приложение которое будет выводить в консоль всю информацию о ваших классах и полях
что нужно
- по возможности взять все классы, которые были у вас написаны до текущего задания
- создать экземпляры для каждого из них (можно скопировать из предыдущих работ так же)
- реализовать метод(ы) которые бы на вход получали некий Object и выводили в консоль абсолютно всю информацию о данном объекте используя Reflection API:
- дерево классов наследников, если есть, иначе ничего не выводить
- список реализуемых интерфейсов
- список конструкторов с параметрами
- список всех полей, в том числе приватных, включая их тип
- список всех методов
- если поле класса является не примитивным, а например любой другой произвольный класс, то по нему так же необходимо вывести всю информацию, если это коллекция то достаточно просто вывести имя коллекции и Generic, то же самое и для методов

пример
pt.SomeClass
--- parents:
--- pt.ParentClass
--- interfaces:
---- pt.SomeInterface
---- fields:
---- fieldName: value java.lang.String
---- fieldNames: java.util.List<String>

и все в таком же духе
дополнительно можете попробовать произвести изменение у приватного поля и у финального поля и посмотреть на результат работы
*/
import lesson5.UADetails;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Lesson7 {
    public static void main(String[] args) {
        // список всех классов из пакетов для 5 и 6 уроков
        Set<Class<?>> classes = getClasses("lesson5");
        classes.addAll(getClasses("lesson6"));
        classes.forEach(System.out::println);

        // использую рефлексию для создания сообщений внутри LogGenerator().generate()
        System.out.println("**************************************************************");
        System.out.println("Вывожу список сообщений в формате: 'класс объекта': 'порядковый номер'. 'время': 'логин' - 'действие'");
        new LogGenerator().generate().forEach(l -> System.out.println(l.getClass() + ": " +l.toString()));

        // вывод информации об объектах
        System.out.println("**************************************************************");
        System.out.println("Сообщение от службы наружного наблюдения за мистером Бондом");
        ObjectExplorer.explore(new UADetails("My name is Bond", "JBond", "James Bond"));
        System.out.println("**************************************************************");
        System.out.println("Отдельно интерфейсы для ниточки:");
        ObjectExplorer.exploreInterfaces(new MyThread());
    }

    private static Set<Class<?>> getClasses(String packageName){
        URL url = Thread.currentThread().getContextClassLoader().getResource(packageName);
        return Arrays.stream(new File(url.getFile()).listFiles())
                .map(File::getName)
                .filter(s -> s.endsWith(".class"))
                .map(s -> packageName + "." +s.substring(0,s.length()-6))
                .map(s -> {
                            try {
                                return Class.forName(s);
                            } catch (ClassNotFoundException e){
                                return null; // как принято обрабатывать это исключение?
                            }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private static class MyThread extends Thread implements RandomAccess { // добавил маркерный интерфейс только чтобы увидеть его в списке, прошу не бить лопатой
    }

}


