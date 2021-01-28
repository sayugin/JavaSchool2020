package spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;


@Component
public class StudentsFileReader implements StudentsReader {

    private static final Logger logger = LoggerFactory.getLogger(StudentsFileReader.class);

    private Map<String, Integer> students;

    @Value("${students.file.filename}")
    private String fileName;

    @Autowired
    public StudentsFileReader(Map<String, Integer> students) {
        this.students = students;
    }

    @Override
    public void read() {
        logger.debug("Пытаюсь прочитать файл " + fileName);
        try {
            Files.lines(Paths.get(fileName), StandardCharsets.UTF_8).forEach(this::parse);
        } catch (IOException e) {
            logger.error("Ошибка при чтении файла: " + e);
        }
        logger.debug("... Готово");
    }

    private void parse(String line) {
        Object[] arr = Arrays.stream(line.split(":")).map(String::trim).toArray();
        String studentName = arr[0].toString();
        if (studentName.isEmpty()) {
            logger.warn("Странный студент нынче пошел");
            return;
        }
        try {
            Integer grade = arr.length == 1 ? null : Integer.valueOf(arr[1].toString());
            students.put(studentName, grade);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            students.put(studentName, null);
        }

    }
}
