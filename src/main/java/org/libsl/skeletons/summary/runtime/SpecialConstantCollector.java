package org.libsl.skeletons.summary.runtime;

import org.libsl.skeletons.summary.Annotations;
import org.libsl.skeletons.summary.ClassSummary;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.Predicate;

final class SpecialConstantCollector {
    private static final List<String> SPECIAL_PRIVATE_CONSTANTS = List.of(
            "serialVersionUID",
            "serialPersistentFields"
    );

    private final Class<?> source;
    private final ClassSummary summary;
    private final boolean useGenerics;

    SpecialConstantCollector(final Class<?> source, final ClassSummary summary, final boolean useGenerics) {
        this.source = source;
        this.summary = summary;
        this.useGenerics = useGenerics;
    }

    private void collectSpecialConstant(final Field field) {
        try {
            final var name = field.getName();
            final var type = getFieldType(field);
            final var constant = summary.addSpecialConstant(name, type);

            final var mods = field.getModifiers();
            constant.annotations.addAll(Annotations.modifiersToAnnotations(mods));

            field.setAccessible(true);
            constant.value = getFieldValue(field);
        } catch (IllegalAccessException ignored) {
        }
    }

    private String getFieldType(final Field field) {
        // TODO: generics
        return TypeSplitter.split(field.getType()).simpleType;
    }

    private String getFieldValue(final Field field) throws IllegalAccessException {
        if (field.getType().isArray())
            return "[]";

        return String.valueOf(field.get(null));
    }

    private void collectSpecialConstant(final String name) {
        // ignore already collected values
        if (summary.specialConstants.containsKey(name))
            return;

        try {
            final var field = source.getDeclaredField(name);
            final var mods = field.getModifiers();

            if (field.isSynthetic() || !Modifier.isStatic(mods) || !Modifier.isFinal(mods))
                throw new RuntimeException("Unable to extract field <" + name + ">");

            collectSpecialConstant(field);
        } catch (NoSuchFieldException ignored) {
        }
    }

    private void collectOtherConstants(final Predicate<Field> fFilter) {
        for (var field : source.getDeclaredFields())
            if (fFilter.test(field)) {
                collectSpecialConstant(field);

                // add modifier (special case: `fun`-ctions are public by default)
                if (Modifier.isPublic(field.getModifiers()))
                    summary.specialConstants.computeIfPresent(field.getName(), (k, sc) -> {
                        sc.annotations.add("@public");
                        return sc;
                    });
            }
    }

    public void findConstants(final Predicate<Field> otherFieldsFilter) {
        for (var constantName : SPECIAL_PRIVATE_CONSTANTS)
            collectSpecialConstant(constantName);

        collectOtherConstants(otherFieldsFilter);
    }
}
