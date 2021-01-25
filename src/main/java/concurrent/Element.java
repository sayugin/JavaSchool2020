package concurrent;

import lombok.Data;
import java.util.List;

@Data
public class Element implements Comparable<Element> {

    // делаю отдельные счетчики для каждого типа детальки чтобы избежать мешанины в логе
    //private static AtomicLong counter = new AtomicLong(0);
    private static long counterA;
    private static long counterB;
    private static long counterC;
    private static long counterМодуль;
    private static long counterВинтик;

    private String detailType; // не хочу делать отдельный класс под каждый тип детали, ограничусь строкой
    private Long id; // хочу отличать детали одного типа друг от друга
    private List<Element> elements;

    public Element(String detailType) {
        //this.id = counter.addAndGet(1);
        switch (detailType) {
            case "a": this.id = ++counterA;
            break;
            case "b": this.id = ++counterB;
            break;
            case "c": this.id = ++counterC;
            break;
            case "модуль": this.id = ++counterМодуль;
            break;
            case "винтик": this.id = ++counterВинтик;
            break;
            default: this.id = 0L;
        }
        this.detailType = detailType;
    }

    public Element(String detailType, List<Element> elements) {
        this(detailType);
        this.elements = elements;
    }

    @Override
    public String toString() {
        if (this.elements == null)
            return this.detailType + this.id;
        else
            return this.detailType + this.id + "(" + elements + ")";
    }

   @Override
    public int compareTo(Element o) {
        return (this.getDetailType()+this.elements).compareTo(o.getDetailType()+o.elements);
    }
}
