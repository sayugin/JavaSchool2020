package spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Lazy
public class StudentsFileWriter implements StudentsWriter {

    private static final Logger logger = LoggerFactory.getLogger(StudentsFileWriter.class);

    @Value("${students.writer.filename}")
    private String fileName;

    @Override
    public void write(Map<String, Integer> students) {
        logger.debug("Пытаюсь записать файл " + fileName);
        try {
            Files.write(Paths.get(fileName), students.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.toList()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Не удалось записать файл: " + e);
        }
        logger.debug("... Готово");
    }

    @PostConstruct
    private void init() {
        logger.debug("Я родился");
    }
}
