package cz.lzaruba.sonar.scm.utils;

import java.util.Map;

public class PropertyUtils {

    private static final String PROPERTY_PREFIX = "sonar.analysis.";

    public String p(Map<String, String> properties, String key) {
        String value = properties.get(PROPERTY_PREFIX + key);
        if (value == null) {
            throw new IllegalStateException(String.format("Missing property '%s'", PROPERTY_PREFIX + key));
        }
        return value;
    }
}
