package org.libsl.skeletons.util;

public final class UnsafeUtils {
    private UnsafeUtils() {
    }

    public static final class Panic extends Error {
        private Panic(final Throwable e) {
            super(e);
        }
    }

    public static <T> T unchecked(final UnsafeCallable<T> c) {
        try {
            return c.call();
        } catch (Exception e) {
            throw new Panic(e);
        }
    }

    public static void unchecked(final UnsafeRoutine c) {
        try {
            c.call();
        } catch (Exception e) {
            throw new Panic(e);
        }
    }

    @FunctionalInterface
    public interface UnsafeCallable<T> {
        T call() throws Exception;
    }

    @FunctionalInterface
    public interface UnsafeRoutine {
        void call() throws Exception;
    }
}
