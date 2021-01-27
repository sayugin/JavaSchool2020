package spring;

import org.springframework.stereotype.Component;

@Component
public class CommonGradeValidator implements GradeValidator {

    @Override
    public boolean validate(String studentName, int grade) {
        return grade >= 1 && grade <= 5;
    }
}
