package spring;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Scanner;

@Data
@Component
public class Grader {

    private static final Logger logger = LoggerFactory.getLogger(Grader.class);

    private Map<String, Integer> students;

    private StudentsReader reader;
    private StudentsAssessor assessor;
    private StudentsWriter writer;

    @Autowired
    public Grader(StudentsReader reader, @Lazy StudentsAssessor assessor, @Lazy StudentsWriter writer) {
        this.reader = reader;
        this.assessor = assessor;
        this.writer = writer;
    }

    public void handle() {
        System.err.println("Для корректного запуска приложения нужно подправить параметры в application.properties");
        System.err.println("Готовый файлик со списком студентов имеется в ресурсах, но можно сделать и свой");
        System.err.println("Оценки вводятся с консоли и валидируются");
        System.err.println("  - для всех студентов оценка должна быть в диапазоне от одного до пяти");
        System.err.println("  - если попадется студент с именем Иосиф Виссарионович, то не уйдет без максимального балла");
        System.err.print("Для продолжения нужно жмякнуть Enter");
        new Scanner(System.in).nextLine();

        logger.info("Поехали");
        students = reader.read();
        if (students.size() != 0) {
            assessor.assess(students);
            writer.write(students);
        }
        logger.info("Все, приехали");
    }

}
