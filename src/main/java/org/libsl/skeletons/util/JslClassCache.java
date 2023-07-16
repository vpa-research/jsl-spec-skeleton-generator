package org.libsl.skeletons.util;

import java.util.HashMap;
import java.util.Map;

public final class JslClassCache {
    private JslClassCache() {}

    private static final Map<String, Class<?>> CLASSES = new HashMap<>();

    public static Class<?> findClass(final String name) {
        return CLASSES.computeIfAbsent(name, k -> {
            try {
                return Class.forName(k);
            } catch (ClassNotFoundException e) {
                return null;
            }
        });
    }
}
