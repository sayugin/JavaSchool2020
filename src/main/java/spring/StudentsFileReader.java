package spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

@Component
public class StudentsFileReader implements StudentsReader {

    private static final Logger logger = LoggerFactory.getLogger(StudentsFileReader.class);

    @Value("${students.reader.filename}")
    private String fileName;

    @Override
    public Map<String, Integer> read() {
        Map<String, Integer> map = new TreeMap<>();
        logger.debug("Пытаюсь прочитать файл " + fileName);
        try {
            Files.lines(Paths.get(fileName), StandardCharsets.UTF_8).forEach(s -> map.put(s, null));
        } catch (IOException e) {
            logger.error("Ошибка при чтении файла: " + e);
        }
        logger.debug("... Готово");
        return map;
    }
}
