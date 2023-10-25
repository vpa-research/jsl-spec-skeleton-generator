package org.libsl.skeletons.util;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;

import static org.libsl.skeletons.util.UnsafeUtils.unchecked;

public final class ReflectionUtils {
    private static final Map<Class<?>, String> PRIMITIVE_DESCRIPTORS;

    static {
        PRIMITIVE_DESCRIPTORS = Map.of(
                byte.class, "B",
                char.class, "C",
                short.class, "S",
                int.class, "I",
                long.class, "J",
                float.class, "F",
                double.class, "D",
                void.class, "V",
                boolean.class, "Z"
        );
    }

    private ReflectionUtils() {
    }

    /**
     * Returns the internal name of {@code clazz} (also known as the descriptor).
     */
    public static String getDescriptor(final Class<?> clazz) {
        final var primitiveSignature = PRIMITIVE_DESCRIPTORS.get(clazz);
        if (primitiveSignature != null)
            return primitiveSignature;
        else if (clazz.isArray())
            return "[" + getDescriptor(clazz.getComponentType());
        else
            return "L" + clazz.getName().replace('.', '/') + ";";
    }

    public static String getDescriptor(final Method method) {
        final var result = new StringJoiner("", "(", ")");

        for (var pType : method.getParameterTypes())
            result.add(getDescriptor(pType));

        return result + getDescriptor(method.getReturnType());
    }

    public static String getDescriptor(final Constructor<?> constructor) {
        final var result = new StringJoiner("", "(", ")");

        for (var pType : constructor.getParameterTypes())
            result.add(getDescriptor(pType));

        return result + "V";
    }

    public static void suppressIllegalAccessWarning() {
        try {
            final var theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            final var u = (Unsafe) theUnsafe.get(null);

            final var cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            final var logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
        } catch (Exception ignore) {
        }
    }

    // https://stackoverflow.com/a/52335318
    public static String currentMethodName() {
        return StackWalker.getInstance()
                .walk(s -> s.skip(1).findFirst())
                .orElseThrow()
                .getMethodName();
    }

    // https://stackoverflow.com/a/52335318
    public static String callerMethodName() {
        return StackWalker.getInstance()
                .walk(s -> s.skip(2).findFirst())
                .orElseThrow()
                .getMethodName();
    }

    public static Class<?> callerClass() {
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(s -> s.skip(2).findFirst())
                .orElseThrow()
                .getDeclaringClass();
    }

    public static Class<?> currentClass() {
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(s -> s.skip(1).findFirst())
                .orElseThrow()
                .getDeclaringClass();
    }

    private static String getSignatureNoReturnType(final Method method) {
        final var descriptor = getDescriptor(method);

        final var bracketIndex = descriptor.indexOf(')');
        final var name = method.getName();
        final var parameters = descriptor.substring(0, bracketIndex + 1);

        return name + parameters;
    }

    public static Method[] getMethodsDirect(final Class<?> origin,
                                            final Predicate<Method> methodFilter) {
        final var result = new ArrayList<Method>();

        for (var m : origin.getDeclaredMethods())
            if (methodFilter.test(m))
                result.add(m);

        return result.toArray(new Method[0]);
    }

    public static Method[] getMethodsInherited(final Class<?> origin,
                                               final Predicate<Method> methodFilter) {
        final var result = new HashMap<String, Method>();

        final var originMods = origin.getModifiers();
        final var collectInterfaceMethods = Modifier.isInterface(originMods) || Modifier.isAbstract(originMods);

        final var visitedClasses = Collections.newSetFromMap(new IdentityHashMap<>());
        final var queue = new ArrayDeque<Class<?>>();

        // interfaces (regular methods)
        final var originParent = origin.getSuperclass();
        if (originParent != null)
            Collections.addAll(queue, originParent.getInterfaces());
        Collections.addAll(queue, origin.getInterfaces());

        while (!queue.isEmpty()) {
            final var clazz = queue.removeLast();

            if (visitedClasses.contains(clazz))
                continue;
            visitedClasses.add(clazz);

            for (var m : clazz.getDeclaredMethods()) {
                final var mods = m.getModifiers();

                if (Modifier.isAbstract(mods) && !collectInterfaceMethods)
                    continue;

                if (!Modifier.isStatic(mods) && Modifier.isPublic(mods))
                    result.put(getSignatureNoReturnType(m), m);
            }

            Collections.addAll(queue, clazz.getInterfaces());
        }

        // superclasses (regular methods)
        queue.add(origin);
        if (originParent != null)
            queue.add(originParent);
        while (!queue.isEmpty()) {
            final var clazz = queue.removeLast();

            if (visitedClasses.contains(clazz))
                continue;
            visitedClasses.add(clazz);

            for (var m : clazz.getDeclaredMethods()) {
                final var mods = m.getModifiers();

                if (!Modifier.isStatic(mods) && methodFilter.test(m))
                    result.put(getSignatureNoReturnType(m), m);
            }

            final var parentClass = clazz.getSuperclass();
            if (parentClass != null)
                queue.add(parentClass);
        }

        // static methods from the source class only! (cannot be overridden by subclasses)
        for (var m : origin.getDeclaredMethods()) {
            final var mods = m.getModifiers();

            if (Modifier.isStatic(mods) && Modifier.isPublic(mods))
                result.put(getSignatureNoReturnType(m), m);
        }

        return result.values().toArray(new Method[0]);
    }

    public static ReflectionAccessHelper debugFrom(final Object source) {
        return new ReflectionAccessHelper(source);
    }

    public static final class ReflectionAccessHelper {
        private final Object src;
        private final Class<?> srcClass;

        private ReflectionAccessHelper(final Object src) {
            this.src = src;
            this.srcClass = src.getClass();
        }

        public <X> CallHelper<X> callPrivate(final String methodName, final Class<X> returnType) {
            return unchecked(() -> {
                final var targetMethod = srcClass.getDeclaredMethod(methodName);

                final var sourceRef = getSourceRef(methodName);
                checkInaccessibility(sourceRef, targetMethod.getModifiers());
                checkReturnType(sourceRef, targetMethod.getReturnType(), returnType);

                targetMethod.setAccessible(true);

                return new CallHelper<>(src, targetMethod);
            });
        }

        private Field fetchPrivateField(final String fieldName, final Class<?> fieldType) {
            return unchecked(() -> {
                final var field = srcClass.getDeclaredField(fieldName);

                final var sourceRef = getSourceRef(fieldName);
                checkInaccessibility(sourceRef, field.getModifiers());
                checkReturnType(sourceRef, field.getType(), fieldType);

                field.setAccessible(true);
                return field;
            });
        }

        public <Q> Q getPrivate(final String fieldName, final Class<Q> fieldType) {
            return unchecked(() -> fieldType.cast(fetchPrivateField(fieldName, fieldType).get(src)));
        }

        public void setPrivate(final String fieldName, final Object value) {
            unchecked(() -> fetchPrivateField(fieldName, value.getClass()).set(src, value));
        }

        private String getSourceRef(final String targetItem) {
            return src.getClass().getSimpleName() + "." + targetItem;
        }

        private static void checkInaccessibility(final String source, final int flags) {
            if (Modifier.isPublic(flags))
                throw new RuntimeException(String.format("%s is PUBLIC and can be accessed by normal means", source));
        }

        private static void checkReturnType(final String source,
                                            final Class<?> actual,
                                            final Class<?> expected) {
            if (!expected.isAssignableFrom(actual))
                throw new RuntimeException(String.format("%s has unexpected return type:\n\texpected: %s,\n\tactual: %s",
                        source,
                        expected, actual));
        }

        public static final class CallHelper<T> {
            private final Object source;
            private final Method targetMethod;

            private CallHelper(final Object source, final Method targetMethod) {
                this.source = source;
                this.targetMethod = targetMethod;
            }

            @SuppressWarnings("unchecked")
            public T with(final Object... args) {
                return unchecked(() -> (T) targetMethod.invoke(source, args));
            }
        }
    }
}
