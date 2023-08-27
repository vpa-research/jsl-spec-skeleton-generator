package org.libsl.skeletons.summary.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

final class ImportCollector {
    private static final String INTERFACES_FILE = "_interfaces";
    private static final Set<Class<?>> IGNORE = Set.of(
            Object.class,
            Class.class,
            String.class
    );

    private final Class<?> source;
    private final Collection<String> outImportReferences;
    private final boolean includeInheritedMethods;

    public ImportCollector(final Class<?> source,
                           final Collection<String> outImportReferences,
                           final boolean includeInheritedMethods) {
        this.source = source;
        this.outImportReferences = outImportReferences;
        this.includeInheritedMethods = includeInheritedMethods;
    }

    private void visitType(final Class<?> t) {
        // WARNING: public inner classes and interfaces are expecting to be declared in the enclosing one
        if (t == null || t.isPrimitive() || t == source || IGNORE.contains(t) || t.isMemberClass())
            return;

        if (t.isInterface() || Modifier.isAbstract(t.getModifiers()))
            outImportReferences.add(t.getPackageName() + "." + INTERFACES_FILE);
        else
            outImportReferences.add(t.getCanonicalName());
    }

    private void visitConstructor(final Constructor<?> constructor) {
        for (var t : constructor.getParameterTypes())
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
        // base class
        visitType(source.getSuperclass());

        // interfaces
        for (var i : source.getInterfaces())
            visitType(i);

        // direct constructors
        for (var c : source.getDeclaredConstructors())
            if (cFilter.test(c))
                visitConstructor(c);

        // methods
        final var methods = includeInheritedMethods
                ? source.getMethods()
                : source.getDeclaredMethods();

        for (var m : methods)
            if (mFilter.test(m))
                visitMethod(m);
    }
}
