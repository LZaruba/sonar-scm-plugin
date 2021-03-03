package cz.lzaruba.sonar.scm.utils;

import java.util.Map;

public class PropertyUtils {

    public String p(Map<String, String> properties, String key) {
        String value = properties.get(key);
        if (value == null) {
            throw new IllegalStateException(String.format("Missing property '%s'", key));
        }
        return value;
    }
}
