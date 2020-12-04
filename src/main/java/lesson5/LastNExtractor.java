package lesson5;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LastNExtractor<T extends LogEntry> implements Extractor<T> {

    @Override
    public List<T> filter(Collection<T> src,  int count) {
        return src.stream().sorted(Comparator.comparing(LogEntry::getCreated).reversed()).limit(count).collect(Collectors.toList());
    }


}
