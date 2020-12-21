package lesson10.testing;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RemoteFileReadWriter {
    public List<String> readFileToList(Map<String, String> settings) {
        System.out.println("Если вы видите этот лог, то тест написан неправильно");
        return Collections.emptyList();
    }

    public void writeResponse() {
        System.out.println("Если вы видите этот лог, то тест написан неправильно");
    }
}
