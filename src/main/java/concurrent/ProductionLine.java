package concurrent;

import lombok.Data;
import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Stream;

@Data
public class ProductionLine {

    private final byte capacity = 3;

    private BlockingQueue<Element> queueA;
    private BlockingQueue<Element> queueB;
    private BlockingQueue<Element> queueC;
    private BlockingQueue<Element> queueМодуль;
    private BlockingQueue<Element> queueВинтик;
    private AtomicBoolean isStopped;

    private ConcurrentSkipListSet<Element> lostElements; // список для потеряшек

    Function<Map.Entry<String, Long>, Element> detailSupplier = entry -> {
        try {
            Thread.sleep(entry.getValue());
            return new Element(entry.getKey());
        } catch (InterruptedException e) {
            return null;
        }
    };

    public ProductionLine() {
        queueA = new ArrayBlockingQueue<>(capacity);
        queueB = new ArrayBlockingQueue<>(capacity);
        queueC = new ArrayBlockingQueue<>(capacity);
        queueМодуль = new ArrayBlockingQueue<>(capacity);
        queueВинтик = new ArrayBlockingQueue<>(capacity);
        lostElements = new ConcurrentSkipListSet<>();
        isStopped = new AtomicBoolean(false);
    }

    private Stream<Element> supply(String detailType, long millis) {
        return Stream.generate(() -> new AbstractMap.SimpleEntry<>(detailType, millis)).map(detailSupplier);
    }

    @SneakyThrows
    public void produce() {
        //ScheduledExecutorService serviceMonitoring = Executors.newScheduledThreadPool(1);
        //serviceMonitoring.scheduleAtFixedRate(() -> System.err.printf("Кол-во деталей на складах: A - %d, B - %d, C - %d, M - %d, Винтик - %d%n", queueA.size(), queueB.size(), queueC.size(), queueМодуль.size(), queueВинтик.size()), 5, 5, TimeUnit.SECONDS);
        ExecutorService service = startThreads();

        // жду пока не заполнится очередь винтиков, а поскольку их потребителей у меня нет, то дальше можно не работать
        while (queueВинтик.size() < capacity)
            Thread.sleep(1000);
        isStopped.getAndSet(true);
        service.shutdownNow();
        //serviceMonitoring.shutdownNow();

        showElements(); // смотрю что ничего не потерялось
    }

    private ExecutorService startThreads() {
        // особого смысла использовать экзекьютор нет, но ради практики пусть будет
        ExecutorService service = Executors.newFixedThreadPool(5);
        System.out.println("Пошла жара");
        service.submit(() -> produceElement("a", 1000, queueA));
        service.submit(() -> produceElement("b", 2000, queueB));
        service.submit(() -> produceElement("c", 3000, queueC));
        service.submit(this::produceModule);
        service.submit(this::produceВинтик);
        return service;
    }

    private void showElements() {
        System.out.println("*** Детальки ***");
        System.out.println(queueA);
        System.out.println(queueB);
        System.out.println(queueC);
        System.out.println("*** Модули ***");
        queueМодуль.forEach(System.out::println);
        System.out.println("*** Винтики ***");
        queueВинтик.forEach(System.out::println);
        System.out.println("*** Потеряшки ***");
        lostElements.forEach(System.out::println);
    }

    public void produceElement(String typeDetail, long prodTime, BlockingQueue<Element> queue) {
        while (!Thread.currentThread().isInterrupted() && !isStopped.get()) {
            supply(typeDetail, prodTime).forEach(e -> {
                try {
                    if (isStopped.get())
                        return;
                    queue.put(e);
                    System.err.println("Деталька " + e + " готова");
                } catch (InterruptedException ex) {
                    lostElements.add(e);
                    System.err.println("Нет места для детальки " + e); // прилетело прерывание, не успел положить детальку на склад
                } catch (NullPointerException ex) {
                    System.err.println("Не успел сделать очередную детальку " + typeDetail); // прилетело прерывание во время производства
                }
            });
        }
    }

    public void produceModule() {
        while (!Thread.currentThread().isInterrupted() && !isStopped.get()) {
            Element a;
            Element b;
            try {
                a = queueA.take();
            } catch (InterruptedException e) {
                System.err.println("a");
                continue;
            }
            try {
                b = queueB.take();
            } catch (InterruptedException e) {
                lostElements.add(a);
                System.err.println("b");
                continue;
            }

            Element block = new Element("модуль", new CopyOnWriteArrayList<>(new Element[]{a, b}));
            try {
                queueМодуль.put(block);
                System.err.printf("Взял %s и %s, хрясь, тыдыщ. Модуль %s готов%n", a, b, block);
            } catch (InterruptedException e) {
                lostElements.add(block);
                System.err.println("Нет места для модуля " + block);
            }
        }
    }

    public void produceВинтик() {
        while (!Thread.currentThread().isInterrupted() && !isStopped.get()) {
            Element c;
            Element m;

            try {
                c = queueC.take();
            } catch (InterruptedException e) {
                System.err.println("c");
                continue;
            }
            try {
                m = queueМодуль.take();
            } catch (InterruptedException e) {
                lostElements.add(c);
                System.err.println("m");
                continue;
            }
            List<Element> elements = m.getElements();
            elements.add(c);
            Element винтик = new Element("винтик", elements);
            try {
                queueВинтик.put(винтик);
                System.err.printf("Взял %s и %s, бац, бац и готово. Винт %s на складе%n", c, m, винтик);
            } catch (InterruptedException e) {
                lostElements.add(винтик);
                System.err.println("Нет места для винта " + винтик);
            }
        }
    }


}

