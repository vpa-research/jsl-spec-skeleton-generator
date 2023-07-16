package org.libsl.skeletons;

import java.util.*;

final class VariableSummary {
    public final String name;
    public final String simpleType;
    public final Set<String> annotations = new LinkedHashSet<>();
    public String value = null;

    public VariableSummary(final String name, final String simpleType) {
        this.name = name;
        this.simpleType = simpleType;
    }
}
