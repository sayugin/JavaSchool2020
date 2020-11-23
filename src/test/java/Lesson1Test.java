import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class Lesson1Test {

    private int[] testData;
    private int[] expected;

    @Before
    public void before() {
        testData = Lesson1.generateArray(1000000,-1000000,1000000);
        //testData = new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        expected = testData.clone();
        Arrays.sort(expected);
    }

    //@Test
    public void bubbleSort() {
        assertArrayEquals(expected, Lesson1.bubbleSort(testData));
    }

    //@Test
    public void insertionSort() {
        assertArrayEquals(expected, Lesson1.insertionSort(testData));
    }

    @Test
    public void quickSort() {
        assertArrayEquals(expected, Lesson1.quickSort(testData));
    }

    @Test
    public void mergeSort() {
        assertArrayEquals(expected, Lesson1.mergeSort(testData));
    }

}