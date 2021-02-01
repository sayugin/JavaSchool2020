package multithreading;

/*
Написать программу эмулирующую дедлок
Написать остановку потока при помощи механизма прерывания
Написать программу содержащую поток производитель (генерирует данные) и поток потребитель (читает данные) и синхронизацию между ними.
*/

import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Operation {

    @SneakyThrows
    public static void main(String[] args) {
        final Account a = new Account(5000, "Хоттабыч");
        final Account b = new Account(3000, "Амаяк наш Акопян");
        Set<Transfer> transfers = new HashSet<>();

        Transfer.needDeadlock(false);
        Thread transferProducer = new Thread(() -> {
            Random rnd = new Random();
            for (int i = 0; i < 10; i++) {
                transfers.add(new Transfer(a, b, rnd.nextInt(500)));
            }
        }); // это будет поток продюсер для третьей задачи, консьюмером будет сама операция
        transferProducer.start();
        transferProducer.join();


        System.err.printf("Initial balance: %s = %d, %s = %d%n", a.getName(), a.getBalance(), b.getName(), b.getBalance());
        ExecutorService service = Executors.newFixedThreadPool(3);
        transfers.forEach(service::submit);
        service.shutdown();
    }

}

