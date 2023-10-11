package org.libsl.skeletons.summary.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.function.Predicate;

final class ImportCollector {
    private final Class<?> source;
    private final Collection<String> outImportReferences;
    private final boolean includeInheritedMethods;

    public ImportCollector(final Class<?> source,
                           final Collection<String> outImportReferences,
                           final boolean includeInheritedMethods) {
        this.source = source;
        this.outImportReferences = outImportReferences;
        this.includeInheritedMethods = includeInheritedMethods;

        this.outImportReferences.add(Object.class.getCanonicalName());
    }

    private void visitType(final Class<?> t) {
        if (t == null || t == source || t.isPrimitive())
            return;

        // WARNING: public inner classes and interfaces are expecting to be declared in the enclosing one
        final var mods = t.getModifiers();
        if (!Modifier.isPublic(mods) || t.isMemberClass())
            return;

        if (t.isArray())
            visitType(t.getComponentType());
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
