package spring;

import org.springframework.stereotype.Component;

@Component
public class SpecialGradeValidator implements GradeValidator {

    @Override
    public boolean validate(String studentName, int grade) {
        return !studentName.equals("Иосиф Виссарионович") || grade == 5;
    }
}
