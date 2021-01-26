package spring;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("grader")
@Data
public class Grader {

    private static final Logger logger = LoggerFactory.getLogger(Grader.class);

    public void handle() {
        logger.info("Старт обработки");
        //todo прочитать файл с фио
        //todo в цикле считать с клавиатуры оценки
        //todo провести валидацию
        //todo сохранить результат в файл
    }

}
