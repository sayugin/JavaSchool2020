package spring.aspects;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
Что-то мне аспекты не больно понравились, т.к. судя по небольшой выборке (по 5 запусков с аспектом и без)
включение профилирования добавляет приблизительно полсекунды (на моем ноуте 3.7 секунды против 3.2)
к запуску даже такого наколеночного приложения.
Дополнительно сделал профилирование через BPP. Время выполнения аннотированных методов без аспектов уменьшилось
с 15-20 мс до 5-8. Делаю вывод что в приложениях, требовательным к производительности, аспекты использовать нельзя
от слова совсем.
*/

@Component
@Aspect
@Slf4j
public class ProfileAspect {

    @Around("@annotation(TimeLog)")
    @SneakyThrows
    public Object aroundCallAt(ProceedingJoinPoint p) {
        long startMillis = System.currentTimeMillis();
        Object[] args = p.getArgs();
        Object retVal = p.proceed(args);
        log.info("Время выполнения {}: {}", p.getSignature(), (System.currentTimeMillis()-startMillis));
        return retVal;
    }
}
