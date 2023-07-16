package org.libsl.skeletons;

import java.util.*;

final class ClassSummary {
    public final String simpleName;
    public final String typeName;
    public final Set<String> imports = new TreeSet<>();
    public final Set<String> annotations = new LinkedHashSet<>();
    public final Map<String, MethodSummary> constructors = new TreeMap<>();
    public final Map<String, MethodSummary> staticMethods = new TreeMap<>();
    public final Map<String, MethodSummary> instanceMethods = new TreeMap<>();
    public final Map<String, VariableSummary> specialConstants = new TreeMap<>();

    private final Map<String, Integer> overloadCounter = new HashMap<>();
    public final Set<String> allGenericTypeVariables = new TreeSet<>();

    public ClassSummary(String name, String typeName) {
        this.simpleName = name;
        this.typeName = typeName;
    }

    private void countMethodOverload(final String functionName) {
        overloadCounter.compute(functionName, (kk, count) -> count != null ? count + 1 : 0);
    }

    public int getOverloadCounter(final String functionName) {
        return overloadCounter.getOrDefault(functionName, 0);
    }

    public MethodSummary addConstructor(final String signature) {
        return constructors.computeIfAbsent(
                signature,
                k -> {
                    countMethodOverload(simpleName);
                    return new MethodSummary(simpleName, signature, MethodSummary.Kind.CONSTRUCTOR, simpleName);
                });
    }

    public MethodSummary addInstanceMethod(final String name, final String signature, final String returnType) {
        return instanceMethods.computeIfAbsent(
                signature,
                k -> {
                    countMethodOverload(name);
                    return new MethodSummary(name, signature, MethodSummary.Kind.METHOD, returnType);
                });
    }

    public MethodSummary addStaticMethod(final String name, final String signature, final String returnType) {
        return staticMethods.computeIfAbsent(
                signature,
                k -> {
                    countMethodOverload(name);
                    return new MethodSummary(name, signature, MethodSummary.Kind.METHOD, returnType);
                });
    }

    public boolean isConstructor(final String signature) {
        return constructors.containsKey(signature);
    }

    public boolean isStaticMethod(final String signature) {
        return staticMethods.containsKey(signature);
    }

    public boolean isInstanceMethod(final String signature) {
        return instanceMethods.containsKey(signature);
    }

    public VariableSummary addSpecialConstant(final String name, final String type) {
        return specialConstants.computeIfAbsent(name, k -> new VariableSummary(name, type));
    }
}
