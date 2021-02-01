package multithreading;

/*
Написать программу эмулирующую дедлок
Написать остановку потока при помощи механизма прерывания
Написать программу содержащую поток производитель (генерирует данные) и поток потребитель (читает данные) и синхронизацию между ними.
*/

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Deadlock {

    @SneakyThrows
    public static void main(String[] args) {
        final Account a = new Account(2000, "Хоттабыч");
        final Account b = new Account(1000, "ДэвидБлэйн");
        Random rnd = new Random();

        System.err.printf("Initial balance: %s = %d, %s = %d%n", a.getName(), a.getBalance(), b.getName(), b.getBalance());
        Transfer.needDeadlock(true);
        Transfer transfer1 = new Transfer(a, b, rnd.nextInt(500));
        Transfer transfer2 = new Transfer(b, a, rnd.nextInt(500));

        Thread t1 = new Thread(transfer1);
        Thread t2 = new Thread(transfer2);

        t1.start();
        t2.start();

        Thread.sleep(3000);
        t2.interrupt();
    }

}

