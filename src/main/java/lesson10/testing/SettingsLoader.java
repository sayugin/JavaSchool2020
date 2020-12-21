package lesson10.testing;

import java.util.Collections;
import java.util.Map;

public class SettingsLoader {

    public Map<String, String> loadSettings() {
        System.out.println("Если вы видите этот лог, то тест написан неправильно");
        return Collections.emptyMap();
    }
}
