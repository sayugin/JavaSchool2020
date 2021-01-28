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
    public Grader(Map<String, Integer> students, StudentsReader reader, @Lazy StudentsAssessor assessor, @Lazy StudentsWriter writer) {
        this.students = students;
        this.reader = reader;
        this.assessor = assessor;
        this.writer = writer;
    }

    public void handle() {
        logger.info("Поехали");
        reader.read();
        if (students.size() != 0) {
            assessor.assess();
            writer.write();
        }
        logger.info("Все, приехали");
    }

}
