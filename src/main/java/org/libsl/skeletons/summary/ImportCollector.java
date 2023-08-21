package org.libsl.skeletons.summary;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

final class ImportCollector {
    private static final String INTERFACES_FILE = "_interfaces";
    private static final Set<Class<?>> IGNORE = Set.of(
            Boolean.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Object.class,
            String.class,
            Class.class
    );

    private final Class<?> source;
    private final Collection<String> outImportReferences;

    public ImportCollector(final Class<?> source, final Collection<String> outImportReferences) {
        this.source = source;
        this.outImportReferences = outImportReferences;
    }

    private void visitType(final Class<?> c) {
        // WARNING: public inner classes and interfaces are expecting to be declared in the enclosing one
        if (c.isPrimitive() || c == source || IGNORE.contains(c) || c.getEnclosingClass() != null)
            return;

        if (c.isInterface() || Modifier.isAbstract(c.getModifiers()))
            outImportReferences.add(c.getPackageName() + "." + INTERFACES_FILE);
        else
            outImportReferences.add(c.getCanonicalName());
    }

    private void visitConstructor(final Executable exe) {
        for (var t : exe.getParameterTypes())
            visitType(t);
    }

    private void visitMethod(final Method method) {
        for (var t : method.getParameterTypes())
            visitType(t);

        visitType(method.getReturnType());
    }

    public void findImports(
            final Predicate<Constructor<?>> cFilter,
            final Predicate<Method> mFilter
    ) {
        for (var c : source.getDeclaredConstructors())
            if (cFilter.test(c))
                visitConstructor(c);

        for (var m : source.getDeclaredMethods())
            if (mFilter.test(m))
                visitMethod(m);
    }
}
