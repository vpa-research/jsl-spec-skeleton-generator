package org.libsl.skeletons.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.libsl.skeletons.util.UnsafeUtils.unchecked;

public final class ReflectionUtils {
    private ReflectionUtils() {
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
