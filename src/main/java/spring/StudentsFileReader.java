package spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.aspects.TimeLog;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;


@Component
@Slf4j
public class StudentsFileReader implements StudentsReader {

    private final Map<String, Integer> students;

    @Value("${students.file.filename}")
    private String fileName;

    @Autowired
    public StudentsFileReader(Map<String, Integer> students) {
        this.students = students;
    }

    @TimeLog
    @Override
    public void read() {
        log.debug("Пытаюсь прочитать файл {}", fileName);
        try {
            getEntryStreamFromFile().forEach(e -> students.put(e.getKey(), e.getValue()));
        } catch (IOException e) {
            log.error("Ошибка при чтении файла: ", e);
        }
        log.debug("... Готово");
    }

    @TimeLog
    @Override
    public Integer readLine(String studentName) {
        log.debug("Пытаюсь прочитать из файла {} оценку для {}", fileName, studentName);
        Integer result = null;
        try {
            OptionalInt grade = getEntryStreamFromFile().filter(e -> e.getKey().equals(studentName)).mapToInt(Map.Entry::getValue).findAny();
            if (grade.isPresent())
                result = grade.getAsInt();
        } catch (IOException e) {
            log.error("Ошибка при чтении файла: ", e);
        }
        log.debug("Прочитал: {}", result);
        return result;
    }

    private Map.Entry<String, Integer> parse(String line) {
        Object[] arr = Arrays.stream(line.split(":")).map(String::trim).toArray();
        String studentName = arr[0].toString();
        if (studentName.isEmpty()) {
            log.warn("Странный студент нынче пошел");
            return null;
        }
        try {
            Integer grade = arr.length == 1 ? null : Integer.valueOf(arr[1].toString());
            return new AbstractMap.SimpleEntry<>(studentName, grade);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return new AbstractMap.SimpleEntry<>(studentName, null);
        }
    }

    private Stream<Map.Entry<String, Integer>> getEntryStreamFromFile() throws IOException {
        return Files.lines(Paths.get(fileName), StandardCharsets.UTF_8)
                .map(this::parse)
                .filter(Objects::nonNull);
    }
}
