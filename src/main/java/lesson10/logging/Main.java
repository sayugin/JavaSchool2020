package lesson10.logging;

import lesson10.logging.v1.StudentPrinter;
import lesson10.logging.v2.NumberOfStudentsPrinter;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();

        students.add(new Student("Иван", "Иванов", 4));
        students.add(new Student("Екатерина", "Захарова", null));
        students.add(new Student("Анна", "Перова", 3));
        students.add(new Student("Олег", "Ильевич", 5));
        students.add(new Student("Михаил", "Омаров", 2));
        students.add(new Student("Лиза", "Ольская", 5));
        students.add(new Student("Инна", "Зимская", null));
        students.add(new Student("Андрей", "Иванчук", 4));
        students.add(new Student("Сергей", "Михайлов", 2));
        students.add(new Student("Иван", "Малахо", 4));

        StudentPrinter studentPrinter = new StudentPrinter();
        studentPrinter.printAllNotPresentStudents(students);
        studentPrinter.printAllStudents(students);
        studentPrinter.printAllStudentsWith2(students);

        NumberOfStudentsPrinter printer = new NumberOfStudentsPrinter();
        printer.printNumberOfAllStudentsPresent(students);
        printer.printNumberOfStudentsWith5And4(students);
    }
}
