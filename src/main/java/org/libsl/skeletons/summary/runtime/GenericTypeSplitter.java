package org.libsl.skeletons.summary.runtime;

import org.libsl.skeletons.sources.runtime.JslClassCache;

import java.lang.reflect.*;
import java.util.ArrayList;

final class GenericTypeSplitter {
    private GenericTypeSplitter() {
    }

    private static TypeSummary splitParameterized(final ParameterizedType type) {
        final var rawTypeName = type.getRawType().getTypeName();
        final var simpleType = JslClassCache.findClass(rawTypeName).getSimpleName();

        final var genericParameters = new ArrayList<String>();
        for (final var t : type.getActualTypeArguments())
            genericParameters.add(t.getTypeName());

        return new TypeSummary(simpleType, genericParameters);
    }

    private static TypeSummary splitWildcard(final WildcardType type) {
        throw new AssertionError("FIXME: Wildcard type encountered");

        /*this.typeStr = "~~~~~WildcardType";//type.getTypeName();
        this.genericParametersStr = "?";*/
    }

    private static TypeSummary splitTypeVariable(final TypeVariable<?> type) {
        return new TypeSummary(
                type.getName()
                // FIXME: missing bounds
        );
    }

    private static TypeSummary splitGenericArray(final GenericArrayType type) {
        return new TypeSummary(
                "array<" + type.getGenericComponentType().getTypeName() + ">"
        );
    }

    private static TypeSummary splitArray(final Class<?> type) {
        return new TypeSummary(
                "array<" + type.getComponentType().getSimpleName() + ">"
        );
    }

    private static TypeSummary splitClass(final Class<?> type) {
        return new TypeSummary(
                type.getSimpleName()
        );
    }

    private static TypeSummary splitPrimitive(final Class<?> type) {
        return new TypeSummary(
                type.getName()
        );
    }

    public static TypeSummary split(final Type type) {
        if (type instanceof WildcardType)
            return splitWildcard((WildcardType) type);

        else if (type instanceof ParameterizedType)
            return splitParameterized((ParameterizedType) type);

        else if (type instanceof TypeVariable)
            return splitTypeVariable((TypeVariable<?>) type);

        else if (type instanceof GenericArrayType)
            return splitGenericArray((GenericArrayType) type);

        else if (type instanceof Class) {
            final var cls = (Class<?>) type;

            if (cls.isArray())
                return splitArray(cls);
            else if (cls.isPrimitive())
                return splitPrimitive(cls);
            else
                return splitClass(cls);
        } else
            throw new AssertionError("Unknown type: " + type);
    }
}
