package lesson7;

import lesson5.LogEntry;
import lesson5.UADetails;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogGenerator {
    private final Message[] messages;
    private final List<LogEntry> entryList;

    public LogGenerator() {
        entryList = new ArrayList<>();
        this.messages = new Message[] {
                new Message("Start server"),

                new Message("Log on", "JBond"),
                new Message("Click", "JBond", "somewhere"),
                new Message("Buy", "JBond", "something good"),
                new Message("Click", "JBond", ", simply click"),
                new Message("Buy", "JBond", "something else"),
                new Message("Log out", "JBond"),

                new Message("Log on", "MStirlitz"),
                new Message("Click", "MStirlitz", "somewhere"),
                new Message("Comment", "MStirlitz", "something"),
                new Message("Click", "MStirlitz", "somewhere"),
                new Message("Log out", "MStirlitz"),

                new Message("Stop server")
        };
    }

    public List<LogEntry> generate() {
        Thread t1 = startThread("JBond");
        Thread t2 = startThread("MStirlitz");
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {/*NOP*/}
        return entryList;
    }

    private void createEntry(Message message) {
        try {
            // забыл геттер для текста сообщения, пришлось выкручиваться
            Field f = message.getClass().getDeclaredField("message");
            f.setAccessible(true);
            String msg = (String) f.get(message);
            f.set(message, msg + "!");

            if (message.getDescription() == null) {
                Constructor<? super UADetails> constructor = UADetails.class.getSuperclass().getConstructor(String.class, String.class);
                this.entryList.add((LogEntry) constructor.newInstance(msg, message.getUser()));
            }
            else {
                Constructor<? super UADetails> constructor = UADetails.class.getConstructor(String.class, String.class, String.class);
                this.entryList.add((LogEntry) constructor.newInstance(msg, message.getUser(), message.getDescription()));
            }
        } catch (NoSuchFieldException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.err.println(e.getMessage());
        }
    }

    private Thread startThread(String login) {

        Thread thread = new Thread(() -> Arrays.stream(this.messages)
                .filter(m -> m.getUser().equals(login))
                .forEach(this::createEntry)
        );
        thread.start();
        return thread;
    }
}
