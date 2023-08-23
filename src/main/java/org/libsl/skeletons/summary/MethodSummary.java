package org.libsl.skeletons.summary;

import java.util.*;

public final class MethodSummary {
    private final String BODY_DEFAULT = "action TODO();";

    public final String simpleName;
    public final String signature;
    public final boolean isConstructor;
    public final String returnType;
    public final Set<String> annotations = new LinkedHashSet<>();
    public final Map<String, VariableSummary> parameters = new LinkedHashMap<>();
    public String body = BODY_DEFAULT;
    public String originClassName = "?";

    public enum Kind {
        METHOD,
        CONSTRUCTOR
    }

    public MethodSummary(final String name,
                         final String signature,
                         final Kind kind,
                         final String returnType) {
        this.simpleName = name;
        this.signature = signature;
        this.isConstructor = kind == Kind.CONSTRUCTOR;
        this.returnType = returnType;
    }

    public static String getSignature(final String name, final Class<?>[] params) {
        final var sj = new StringJoiner(", ", "(", ")");
        for (var clazz : params)
            sj.add(clazz.getSimpleName());

        return params.length != 0 ? name + " " + sj : name;
    }

    public VariableSummary addParameter(final String name, final String type) {
        return parameters.computeIfAbsent(name, k -> new VariableSummary(name, type));
    }
}
