package lesson5;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FunctionalInterface
public interface Extractor<T extends LogEntry> {

    // функция сортирует коллекцию и отбирает из нее cnt элементов
    List<T> filter(Collection<T> src, int cnt);

    static <T extends LogEntry> List<T> collect(Stream<T> stream, int count){
       return stream.limit(count).collect(Collectors.toList());
    }

}
