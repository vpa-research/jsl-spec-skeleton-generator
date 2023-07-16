package org.libsl.skeletons.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Deprecated
public class ReflectiveVisitor {
    protected final Map<Class<?>, Consumer<Object>> visitors = new HashMap<>();

    private void visit(final Set<Object> visited, final Object obj) {
        if (obj == null || visited.contains(obj))
            return;
        visited.add(obj);

        final var clazz = obj.getClass();
        final var q = visitors.get(clazz);
        if (q != null)
            q.accept(obj);

        for (var m : clazz.getMethods())
            if (m.getName().startsWith("get") && m.getParameters().length == 0) {
                try {
                    final var result = m.invoke(obj);
                    visit(visited, result);
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
            }
    }

    public void foo(final Object base) {
        final var visited = new HashSet<>();
        visit(visited, base);
    }
}
