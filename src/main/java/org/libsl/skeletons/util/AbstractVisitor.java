package org.libsl.skeletons.util;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractVisitor<B, P extends AbstractVisitor.ObjectProcessor<B, R>, R> {
    private final Map<Class<? extends B>, P> exactVisitors = new HashMap<>();
    private final Map<Class<? extends B>, P> fuzzyVisitors = new LinkedHashMap<>();

    @FunctionalInterface
    public interface ObjectProcessor<I, O> {
        O call(I obj);
    }

    protected final <E extends B, V extends ObjectProcessor<E, R>> void addVisitorExact(final Class<E> c, final V v) {
        @SuppressWarnings("unchecked") final var p = (P) v;
        exactVisitors.put(c, p);
    }

    protected final <E extends B, V extends ObjectProcessor<E, R>> void addVisitorFuzzy(final Class<E> c, final V v) {
        final var mods = c.getModifiers();
        if (Modifier.isFinal(mods))
            throw new AssertionError("Use non-finalized classes for fuzzy type matching");

        @SuppressWarnings("unchecked") final var p = (P) v;
        fuzzyVisitors.put(c, p);
    }

    protected final ObjectProcessor<B, R> findVisitor(final Class<? extends B> c) {
        // look for exact match first
        final var exact = exactVisitors.get(c);
        if (exact != null)
            return exact;

        // then for anything *similar*
        for (var e : fuzzyVisitors.entrySet())
            if (e.getKey().isAssignableFrom(c))
                return e.getValue();

        // nothing found
        return null;
    }
}
