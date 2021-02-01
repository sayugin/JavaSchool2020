package spring;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@Slf4j
public class Grader {

    private Map<String, Integer> students;
    private Map<String, Integer> cacheMap;

    private StudentsReader reader;
    private StudentsAssessor assessor;
    private StudentsWriter writer;

    @Autowired
    private Grader(Map<String, Integer> students, @Qualifier("cacheMap") Map<String, Integer> cacheMap, StudentsReader reader, @Lazy StudentsAssessor assessor, @Lazy StudentsWriter writer) {
        this.students = students;
        this.reader = reader;
        this.assessor = assessor;
        this.writer = writer;
        this.cacheMap = cacheMap;
    }

    public void handle() {
        log.info("Поехали");
        reader.read();
        if (students.size() != 0) {
            assessor.assess();
            writer.write();
        }
        log.info("Все, приехали");
    }

    public Integer getGradeForStudent(String studentName) {
        log.info("Поехали искать оценку для {}", studentName);

        // убрал кэширование в аспект
        //Integer result = cacheMap.computeIfAbsent(studentName, s -> reader.readLine(s));
        Integer result = reader.readLine(studentName);
        // читаю оценку второй раз, чтобы убедиться что она берется из кэша и вызова reader.readLine не происходит
        result = reader.readLine(studentName);
        // вывожу мапу с кэшем
        log.info(cacheMap.toString());

        if (result == null)
            log.info("Не нашел оценку для {}", studentName);
        else
            log.info("Таки нашел оценку для {}: {}", studentName, result);
        return result;

    }

}
