package spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import spring.aspects.TimeLog;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Lazy
@Slf4j
public class StudentsFileWriter implements StudentsWriter {

    private final Map<String, Integer> students;

    @Value("${students.file.filename}")
    private String fileName;

    @Autowired
    public StudentsFileWriter(Map<String, Integer> students) {
        this.students = students;
    }

    @TimeLog
    @Override
    public void write() {
        log.debug("Пытаюсь записать файл " + fileName);
        try {
            Files.write(Paths.get(fileName), students.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.toList()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Не удалось записать файл: " + e);
        }
        log.debug("... Готово");
    }

    @PostConstruct
    private void init() {
        log.debug("Я родился");
    }
}
