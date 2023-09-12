package org.libsl.skeletons.summary.runtime;

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
        final var mods = c.getModifiers();

        if (c.isSynthetic())
            return false;

        return !Modifier.isAbstract(mods);
    }

    public static boolean isSuitablePublicMethod(final Method m) {
        return isSuitablePublicMethodWithObject(m)
                && m.getDeclaringClass() != Object.class;
    }

    public static boolean isSuitablePublicMethodWithObject(final Method m) {
        final var mods = m.getModifiers();

        if (Modifier.isAbstract(mods))
            return false;

        // Note: bridges are always synthetic
        if (m.isSynthetic())
            return false;

        return Modifier.isPublic(mods);
    }
}
