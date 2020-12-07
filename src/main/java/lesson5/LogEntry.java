package lesson5;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class LogEntry {
    private static AtomicLong seq = new AtomicLong(0);
    private static final Object lock = new Object();

    private final long id;
    private final LocalDateTime created;
    private final String message; // по итогу тут лучше бы подошел енум, да уже неохота переделывать

    public LogEntry(String message){
        // делаю паузу чтобы разнести сообщения во времени
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {/*NOP*/}

        this.id = seq.addAndGet(1);
        this.created = LocalDateTime.now();
        this.message = message;

    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%d. %s: %s", this.getId(), this.getCreated(), this.getMessage());
    }
}
