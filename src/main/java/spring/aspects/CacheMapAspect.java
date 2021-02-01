package spring.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Aspect
public class CacheMapAspect {

    Map<String, Integer> cacheMap;

    @Autowired
    public CacheMapAspect(@Qualifier("cacheMap") Map<String, Integer> cacheMap) {
        this.cacheMap = cacheMap;
    }

    @Around("execution(public Integer spring.StudentsFileReader.readLine(String))")
    public Integer aroundReadLine(ProceedingJoinPoint p) {
        String studentName = (String) p.getArgs()[0];
        return cacheMap.computeIfAbsent(studentName, s -> {
            try {
                return (Integer) p.proceed(p.getArgs());
            } catch (Throwable e) {
                return null;
            }
        });
    }

}
