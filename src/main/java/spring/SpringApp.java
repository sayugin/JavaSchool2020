package spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

import java.util.Map;
import java.util.TreeMap;

@SpringBootApplication
@ComponentScan(basePackages = {"spring","sandbox"})
public class SpringApp implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }

    public void run(String... args) {
        Grader grader = (Grader) context.getBean("grader");
        //grader.handle();
        Integer grade = grader.getGradeForStudent("Владимир Ильич");
        System.out.println(grade == null ? "" : grade); // не знаю что делать с полученной оценкой, просто вывожу ее в консоль
    }

    @Bean
    @Primary
    public Map<String, Integer> getMap() { // есть мысль в мапах поменять Integer на OptionalInt
        return new TreeMap<>();
    }

    @Bean("cacheMap")
    public Map<String, Integer> getCache() {
        return new TreeMap<>();
    }

}



