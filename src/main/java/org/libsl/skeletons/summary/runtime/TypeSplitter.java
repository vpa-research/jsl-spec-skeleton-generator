package org.libsl.skeletons.summary.runtime;

import org.libsl.skeletons.util.StringUtils;

final class TypeSplitter {
    private TypeSplitter() {
    }

    private static TypeSummary splitArray(final Class<?> type) {
        return new TypeSummary(
                "array<" + StringUtils.getCompleteClassName(type.getComponentType()) + ">"
        );
    }

    private static TypeSummary splitClass(final Class<?> type) {
        return new TypeSummary(
                StringUtils.getCompleteClassName(type)
        );
    }

    private static TypeSummary splitPrimitive(final Class<?> type) {
        return new TypeSummary(
                type.getName()
        );
    }

    public static TypeSummary split(final Class<?> type) {
        if (type.isArray())
            return splitArray(type);
        else if (type.isPrimitive())
            return splitPrimitive(type);
        else
            return splitClass(type);
    }
}
