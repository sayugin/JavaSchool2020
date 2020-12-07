package lesson5;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class LastNExtractor<T extends LogEntry> implements Extractor<T> {

    @Override
    public List<T> filter(Collection<T> src,  int count) {
        return Extractor.collect(src.stream().sorted(Comparator.comparing(LogEntry::getCreated).reversed()), count);
    }


}
