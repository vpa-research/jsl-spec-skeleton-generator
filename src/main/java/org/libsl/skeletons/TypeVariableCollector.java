package org.libsl.skeletons;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Predicate;

final class TypeVariableCollector {
    private final Class<?> source;
    private final Collection<String> outTypeParameters;
    private final Collection<Type> visitedTypes = new HashSet<>();

    public TypeVariableCollector(final Class<?> sourceClass, final Collection<String> outTypeParameters) {
        this.source = sourceClass;
        this.outTypeParameters = outTypeParameters;
    }

    private void collectTypeParameter(final Type type) {
        if (type == null || visitedTypes.contains(type))
            return;
        visitedTypes.add(type);

        if (type instanceof TypeVariable) {
            final var variable = (TypeVariable<?>) type;
            outTypeParameters.add(variable.getName());

            collectTypeParameters(variable.getBounds());
        } else if (type instanceof ParameterizedType) {
            final var pt = (ParameterizedType) type;

            collectTypeParameter(pt.getOwnerType());
            collectTypeParameter(pt.getRawType());
            collectTypeParameters(pt.getActualTypeArguments());
        }
    }

    private void collectTypeParameters(final Type[] types) {
        if (types == null)
            return;

        for (var t : types)
            collectTypeParameter(t);
    }

    public void collectTypeParameters(
            final Predicate<Constructor<?>> cFilter,
            final Predicate<Method> mFilter
    ) {
        // class itself
        collectTypeParameters(source.getTypeParameters());

        // constructors
        for (var c : source.getDeclaredConstructors()) {
            if (!cFilter.test(c))
                continue;

            // parameters
            collectTypeParameters(c.getGenericParameterTypes());

            // exceptions
            collectTypeParameters(c.getGenericExceptionTypes());
        }

        // usual methods
        for (var m : source.getDeclaredMethods()) {
            if (!mFilter.test(m))
                continue;

            // parameters
            collectTypeParameters(m.getGenericParameterTypes());

            // exceptions
            collectTypeParameters(m.getGenericExceptionTypes());

            // return value
            collectTypeParameter(m.getGenericReturnType());
        }
    }
}
