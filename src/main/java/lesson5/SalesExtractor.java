package lesson5;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class SalesExtractor<T extends LogEntry> implements Extractor<T> {

    @Override
    public List<T> filter(Collection<T> src,  int count) {
        return Extractor.collect(src.stream().filter(l -> l.getMessage().equals("Buy")).sorted(Comparator.comparing(LogEntry::getCreated).reversed()), count);
    }


}
