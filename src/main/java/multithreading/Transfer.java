package multithreading;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class Transfer extends Thread {

    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    private static boolean needDeadlock;

    private static final int LOCK_WAIT_SEC = 3;
    private static final int MAX_TRANSFER_SEC = 2;

    private final int transferId;

    private final Account accFrom;
    private final Account accTo;
    private final int amount;

    private final Random waitRandom = new Random();

    public Transfer(Account accFrom, Account accTo, int amount) {
        this.transferId = idGenerator.getAndIncrement();
        this.accFrom = accFrom;
        this.accTo = accTo;
        this.amount = amount;
    }

    public static void needDeadlock(boolean generateDeadlock) {
        Transfer.needDeadlock = generateDeadlock;
    }

    @Override
    public void run() {
        if (needDeadlock)
            ахалай_махалай();
        else
            трах_тибидох_тибидох();
    }

    @SneakyThrows
    private void трах_тибидох_тибидох() {
        if (accFrom.getLock().tryLock(LOCK_WAIT_SEC, TimeUnit.SECONDS)) {
            try {
                if (accTo.getLock().tryLock(LOCK_WAIT_SEC, TimeUnit.SECONDS)) {
                    try {
                        accFrom.withdraw(amount);
                        accTo.deposit(amount);
                        System.err.printf("Balance after (%s -> %s, %d): %s = %d, %s = %d%n", accFrom.getName(), accTo.getName(), amount, accFrom.getName(), accFrom.getBalance(), accTo.getName(), accTo.getBalance());
                        Thread.sleep(waitRandom.nextInt(MAX_TRANSFER_SEC * 1000));
                    } finally {
                        accTo.getLock().unlock();
                    }
                }
                else
                    System.err.println("transferId = " + getTransferId() + ": блокировка счета " + accTo.getName());

            } finally {
                accFrom.getLock().unlock();
            }
        }
        else
            System.err.println("transferId = " + getTransferId() + ": блокировка счета " + accFrom.getName());
    }

    private void ахалай_махалай() {
        accFrom.getLock().lock();
        try {
            Thread.sleep(50);
            while (!accTo.getLock().tryLock() && !isInterrupted()) {
                System.err.println("жду...");
                Thread.sleep(1000);
            }
            if (!isInterrupted())
                try {
                    accFrom.withdraw(amount);
                    accTo.deposit(amount);
                    System.err.printf("Balance after (%s -> %s, %d): %s = %d, %s = %d%n", accFrom.getName(), accTo.getName(), amount, accFrom.getName(), accFrom.getBalance(), accTo.getName(), accTo.getBalance());
                } finally {
                    accTo.getLock().unlock();
                }
        } catch (InterruptedException e) {
            System.err.println("больше не жду");
        } finally {
            accFrom.getLock().unlock();
        }
    }

}

