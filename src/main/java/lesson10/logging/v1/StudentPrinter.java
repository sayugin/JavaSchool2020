package lesson10.logging.v1;

import lesson10.logging.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StudentPrinter {

    private static final Logger logger = LoggerFactory.getLogger(StudentPrinter.class);

    public void printAllNotPresentStudents(List<Student> students) {
        //TODO Напечатать список всех студентов, которые отсутствовали на тесте (нет оценки) с уровнем warn
        students.stream().filter(s -> s.getResult() == null).forEach(s -> logger.warn(s.toString()));
    }

    public void printAllStudents(List<Student> students) {
        //TODO Напечатать список всех студентов с уровнем info
        students.forEach(s -> logger.info(s.toString()));
    }

    public void printAllStudentsWith2(List<Student> students) {
        //TODO Напечатать список всех студентов, которые имеют оценку 2 c уровнем error
        students.stream().filter(s -> s.getResult() != null && s.getResult() == 2).forEach(s -> logger.error(s.toString()));
    }
}
