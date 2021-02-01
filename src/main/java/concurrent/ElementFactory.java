package concurrent;

/*
Разработайте имитатор производственной линии, изготавливающей винтики (widget).
Винтик собирается из детали C и модуля, который, в свою очередь, состоит из деталей A и B.
Для изготовления детали A требуется 1 секунда, В - две секунды, С - три секунды.
Для хранения промежуточных компонентов используйте хранилища ограниченного размера.
*/

public class ElementFactory {
    public static void main(String[] args) {
        ProductionLine productionLine = new ProductionLine();
        productionLine.produce();
    }
}
