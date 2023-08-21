package org.libsl.skeletons.sources.runtime;

import java.util.HashMap;
import java.util.Map;

public final class JslClassCache {
    private JslClassCache() {
    }

    private static final Map<String, Class<?>> CLASSES = new HashMap<>();

    public static Class<?> findClass(final String canonicalClassName) {
        return CLASSES.computeIfAbsent(canonicalClassName, name -> {
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException e) {
                return null;
            }
        });
    }
}
