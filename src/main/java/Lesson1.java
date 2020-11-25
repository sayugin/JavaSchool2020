import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/*
1. Напишите программу, которая выводит строку «Hello World!»  в консоль.
2. В переменных a и b хранятся два натуральных числа. Напишите программу, выводящую на экран результат деления a на b с остатком.
   Пример работы программы (a = 21, b = 8): « 21 / 8 = 2 и 5 в остатке »
3. Напишите программу, которая вычислит простые числа в пределах от 2 до 100.
4. Выведите на экран первые n членов последовательности Фибоначчи, где n запрашивается у пользователя посредством консоли (2 < n < 100).
5. Напишите программу, которая объявляет массив целых чисел (10-20 чисел) и реализует алгоритм сортировки заданного массива (на ваш выбор).
*/
public class Lesson1 {

    public static void main(String[] args) {
        // Hello world
        System.out.println("Hello world");

        // результат деления
        int a = 23;
        int b = 8;
        System.out.println(String.format("%d / %d = %d и %d в остатке", a, b, a/b, a%b));

        // простые числа
        StringBuilder sb = new StringBuilder("1");
        for (int i = 2; i < 100; i++) {
            for (int j = 2; (j <= i) && (i==j || (i % j) != 0); j++) {
                if (i==j) {
                    sb.append(" ");
                    sb.append(i);
                }
            }
        }

        System.out.println(sb.toString());

        // последовательность Фибоначчи
        System.out.println("Дай число от 1 до 127");
        printFibo();

        // сортировка массива
        // можно раскомментировать демо ниже, но лучше смотреть через юнит-тесты
        /*int[] arr = generateArray(100, -21556, 37601);
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(bubbleSort(arr)));
        System.out.println(Arrays.toString(insertionSort(arr)));
        System.out.println(Arrays.toString(quickSort(arr)));
        System.out.println(Arrays.toString(mergeSort(arr)));*/
    }

    public static int[] bubbleSort(int arr[]){
        if (arr.length <= 1)
            return arr;

        for (int i=0; i < arr.length; i++)
            for (int j = i+1; j < arr.length; j++)
                if (arr[i] > arr[j])
                    swap(arr, i, j);
        return arr;
    }

    public static int[] insertionSort(int arr[]){
        if (arr.length <= 1)
            return arr;

        for (int i = 1; i < arr.length; i++)
            for (int j = i; j > 0 && arr[j-1] > arr[j]; j--)
                swap(arr,j-1, j);
        return arr;
    }

    public static int[] quickSort(int[] arr) {
        return quickSort(arr, 0, arr.length-1);
    }

    public static int[] quickSort(int[] arr, int leftBorder, int rightBorder) {

        // выставляю маркеры и опорный элемент
        int left = leftBorder;
        int right = rightBorder;
        int pivot = arr[(left + right) / 2];

        // гоняю цикл пока все элементы меньше опорного не окажутся слева от него, а больше - справа
        do {
            // двигаю левый маркер слева направо пока не найду элемент больше чем опорный либо равный ему
            while (arr[left] < pivot)
                left++;

            // двигаю правый маркер справа налево пока не найду элемент меньше чем опорный либо равный ему
            while (arr[right] > pivot)
                right--;

            if (left <= right) {

                // меняю найденные элементы местами
                if (left < right)
                    swap(arr, left, right);

                // сдвигаю маркеры чтобы не пробегать по уже просмотренным элемента
                left++;
                right--;
            }
        } while (left <= right);

        // рекурсивно сортирую подмассивы слева и справа от опорного элемента
        if (leftBorder < right)
            quickSort(arr, leftBorder, right);

        if (left < rightBorder)
            quickSort(arr, left, rightBorder);

        return arr;
    }


    public static int[] mergeSort(int[] arr) {
        return mergeSort(arr, 0, arr.length-1);
    }
    public static int[] mergeSort(int[] arr, int leftBorder, int rightBorder) {

        // делю входной диапазон элементов пополам, запоминаю границу
        int delimiter = leftBorder + ((rightBorder - leftBorder) / 2) + 1;

        // если каждую из половинок можно еще поделить (т.е. в ней более одного элемента), то рекурсивно вызываю функцию
        if (delimiter > 0 && rightBorder > (leftBorder + 1)) {
            mergeSort(arr, leftBorder, delimiter - 1);
            mergeSort(arr, delimiter, rightBorder);
        }

        // на выходе получаю два отсортированных диапазона, сливаю их в один

        // создаю временный массив
        int[] buffer = new int[rightBorder - leftBorder + 1];

        // запоминаю индексы первых элементов обеих половинок
        int left = leftBorder;
        int right = delimiter;

        for (int i = 0; i < buffer.length; i++) {

            // заполняю временный массив, поэлементно выбирая минимальное значение из половинок
            if ((left < delimiter) && (right > rightBorder || arr[left] < arr[right])) {
                buffer[i] = arr[left];
                left++;
            } else {
                buffer[i] = arr[right];
                right++;
            }
        }

        // записываю полученный отсортированный массив в массив-источник и возвращаю результат
        System.arraycopy(buffer, 0, arr, leftBorder, buffer.length);
        return arr;
    }

    private static void swap(int[] arr, int a, int b) {
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }

    public static int[] generateArray(int cnt, int min, int max) {
        return new Random(System.nanoTime()).ints(cnt, min,max + 1).toArray();
    }

    public static void printFibo() {
        byte n;

        try {
            n = new Scanner(System.in).nextByte();
            if (n < 1)
                throw new InputMismatchException();
        }
        catch (InputMismatchException e) {
            System.out.println("Повторяю: хочу число от 1 до 127! А вы мне что суете?");
            return;
        }

        BigInteger p1 = new BigInteger("1");
        BigInteger p2 = new BigInteger("0");
        BigInteger c;

        switch (n) {
            case 1:
                System.out.println("0");
                break;
            case 2:
                System.out.println("0 1");
                break;
            default:
                StringBuilder sb = new StringBuilder("0 1");
                for (byte i = 0; i <= n - 1 - 2; i++) {
                    c = p1.add(p2);
                    sb.append(" ");
                    sb.append(c);
                    p2 = p1;
                    p1 = c;
                }
                System.out.println(sb.toString());
        }

    }

}
