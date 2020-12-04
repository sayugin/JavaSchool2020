package lesson5;

import java.util.Collection;
import java.util.List;

@FunctionalInterface
public interface Extractor<T extends LogEntry> {

    // функция сортирует коллекцию и отбирает из нее cnt элементов
    List<T> filter(Collection<T> src, int cnt);

}
