package org.libsl.skeletons.summary.runtime;

import java.util.List;
import java.util.Objects;

final class TypeSummary {
    public static final List<String> NO_TYPE_ARGUMENTS = List.of();

    public final String simpleType;
    public final List<String> typeArgs;
    public final boolean hasTypeArguments;

    TypeSummary(final String simpleType, final List<String> typeArgs) {
        this.simpleType = Objects.requireNonNull(simpleType);
        this.typeArgs = Objects.requireNonNull(typeArgs);
        this.hasTypeArguments = !typeArgs.isEmpty();
    }

    TypeSummary(final String simpleType) {
        this(simpleType, NO_TYPE_ARGUMENTS);
    }
}
