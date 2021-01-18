package lesson10.logging.v2;
import lesson10.logging.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NumberOfStudentsPrinter {
    private static final Logger logger = LoggerFactory.getLogger(NumberOfStudentsPrinter.class);

    public void printNumberOfAllStudentsPresent(List<Student> students) {
        //TODO Напечатать количество студентов, которые присутствовали на тесте (имеют оценку) с уровнем info
        students.stream().filter(s -> s.getResult() != null).forEach(s -> logger.info(s.toString()));
    }

    public void printNumberOfStudentsWith5And4(List<Student> students) {
        //TODO Напечатать количество студентов, имеющих оценку 5, и имеющих оценку 4 (значения суммировать не надо). Записать необходимо в одну строку.
        // Лог должен выводиться с уровнем debug
        Map<Integer, Long> map = students.stream().filter(s -> s.getResult() != null && (s.getResult() == 4 || s.getResult() == 5)).collect(Collectors.groupingBy(Student::getResult, Collectors.counting()));
        logger.debug("{} {}", "Кол-во студентов с оценками 4 и 5: ", map.values().stream().map(Object::toString).reduce("", (s1, s2) -> s1 + (s1.length() == 0 ? "" : " и ") + s2));
    }
}
