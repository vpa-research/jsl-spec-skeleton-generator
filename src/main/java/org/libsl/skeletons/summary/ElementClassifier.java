package org.libsl.skeletons.summary;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

final class ElementClassifier {
    private ElementClassifier() {
    }

    public static boolean isSuitablePublicField(final Field field) {
        final var mods = field.getModifiers();
        return !field.isSynthetic()
                && Modifier.isPublic(mods)
                && Modifier.isStatic(mods)
                && Modifier.isFinal(mods);
    }

    public static boolean isSuitableConstructor(final Constructor<?> c) {
        return !c.isSynthetic();
    }

    public static boolean isSuitablePublicMethod(final Method m) {
        final var mods = m.getModifiers();
        // Note: bridges are always synthetic
        return !m.isSynthetic() && Modifier.isPublic(mods);
    }
}
