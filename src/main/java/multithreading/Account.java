package multithreading;

import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private String name;
    private int balance;
    private Lock lock;
    //private final LongAdder failCounter = new LongAdder();

    public Account(int initialBalance, String name) {
        this.balance = initialBalance;
        this.name = name;
        this.lock = new ReentrantLock();
    }

    public int getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public Lock getLock() {
        return lock;
    }

    public void withdraw(int amount) {
        this.balance -= amount;
    }

    public void deposit(int amount) {
        this.balance += amount;
    }

    /*public void incFailedTransferCount() {
        failCounter.increment();
    }

    public long getFailCount() {
        return failCounter.sum();
    }*/
}

