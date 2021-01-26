package spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
@Lazy
public class StudentsConsoleAssessor implements StudentsAssessor {

    private static final Logger logger = LoggerFactory.getLogger(StudentsConsoleAssessor.class);
    private static final Scanner scanner = new Scanner(System.in);

    @Autowired
    List<GradeValidator> validators;

    @Override
    public void assess(Map<String, Integer> students) {
        logger.debug("Считываю оценки с консоли");
        System.out.println("Скажи-ка мне, мил человек, свое мнение о следующих товарищах");
        for (String studentName: students.keySet()) {
            boolean check = false;
            int grade = 0;
            while (!check) {
                System.out.print(studentName + ": ");
                grade = scanner.nextInt();
                for (GradeValidator validator: validators) {
                    check = validator.validate(studentName, grade);
                    if (!check) {
                        System.out.println("Подумай и попробуй еще раз");
                        break;
                    }
                }
            }
            students.put(studentName, grade);
        }
        logger.debug("... Готово");
    }

    @PostConstruct
    private void init() {
        logger.debug("Я родился");
    }

}
