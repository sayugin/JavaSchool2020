package lesson5;

import java.time.LocalDateTime;
import java.util.Random;

public class LogEntry {
    private static long seq = 0L;
    private static final Object lock = new Object();

    private final long id;
    private final LocalDateTime created;
    private final String message; // по итогу тут лучше бы подошел енум, да уже неохота переделывать

    public LogEntry(String message){
        // делаю паузу чтобы разнести сообщения во времени
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {/*NOP*/}

        // на всякий случай делаю синхронизацию чтобы не получить одинаковый ид в разных потоках, java.util.concurrent не использую ибо еще маленький
        synchronized (lock) {
            this.id = ++seq;
            this.created = LocalDateTime.now();
            this.message = message;
        }
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
