package org.libsl.skeletons.summary.runtime;

import org.libsl.skeletons.sources.runtime.JslClassCache;

import java.lang.reflect.*;
import java.util.ArrayList;

final class GenericTypeSplitter {
    private GenericTypeSplitter() {
    }

    private static GenericTypeSummary splitParameterized(final ParameterizedType type) {
        final var rawTypeName = type.getRawType().getTypeName();
        final var simpleType = JslClassCache.findClass(rawTypeName).getSimpleName();

        final var genericParameters = new ArrayList<String>();
        for (final var t : type.getActualTypeArguments())
            genericParameters.add(t.getTypeName());

        return new GenericTypeSummary(simpleType, genericParameters);
    }

    private static GenericTypeSummary splitWildcard(final WildcardType type) {
        throw new AssertionError("FIXME: Wildcard type encountered");

        /*this.typeStr = "~~~~~WildcardType";//type.getTypeName();
        this.genericParametersStr = "?";*/
    }

    private static GenericTypeSummary splitTypeVariable(final TypeVariable<?> type) {
        return new GenericTypeSummary(
                type.getName()
                // FIXME: missing bounds
        );
    }

    private static GenericTypeSummary splitGenericArray(final GenericArrayType type) {
        return new GenericTypeSummary(
                "array<" + type.getGenericComponentType().getTypeName() + ">"
        );
    }

    private static GenericTypeSummary splitArray(final Class<?> type) {
        return new GenericTypeSummary(
                "array<" + type.getComponentType().getSimpleName() + ">"
        );
    }

    private static GenericTypeSummary splitClass(final Class<?> type) {
        return new GenericTypeSummary(
                type.getSimpleName()
        );
    }

    private static GenericTypeSummary splitPrimitive(final Class<?> type) {
        return new GenericTypeSummary(
                type.getName()
        );
    }

    public static GenericTypeSummary split(final Type type) {
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
