package org.libsl.skeletons.util;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A nullable alternative to {@link java.util.Optional}.
 *
 * @param <T> value type.
 */
public final class ValueContainer<T> {
    private static final ValueContainer<?> EMPTY = new ValueContainer<>();

    private final T value;
    private final boolean present;

    private ValueContainer() {
        this.value = null;
        this.present = false;
    }

    private ValueContainer(final T value) {
        this.value = value;
        this.present = true;
    }

    public boolean isPresent() {
        return present;
    }

    public boolean isEmpty() {
        return !present;
    }

    public T get() {
        if (!present)
            throw new NoSuchElementException("No value present");

        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof ValueContainer))
            return false;

        final var other = (ValueContainer<?>) obj;
        return present == other.present && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(present) ^ Objects.hashCode(value);
    }

    @SuppressWarnings("unchecked")
    public static <O> ValueContainer<O> empty() {
        return (ValueContainer<O>) EMPTY;
    }

    public static <O> ValueContainer<O> of(final O value) {
        return new ValueContainer<>(value);
    }

    public static <O> ValueContainer<O> ofNullable(final O value) {
        return value == null ? empty() : of(value);
    }
}
