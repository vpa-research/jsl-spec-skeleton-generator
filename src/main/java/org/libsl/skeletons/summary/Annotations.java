package org.libsl.skeletons.summary;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

public final class Annotations {
    private Annotations() {
    }

    private static final int VARARGS_FLAG = 0x80;

    public static final String PUBLIC = "@public";
    public static final String PROTECTED = "@protected";
    public static final String PRIVATE = "@private";

    public static final String FINAL = "@final";
    public static final String STATIC = "@static";
    public static final String SYNCHRONIZED = "@synchronized";
    public static final String VOLATILE = "@volatile";
    public static final String STRICT = "@strict";
    public static final String VARARGS = "@varargs";
    public static final String DEFAULT = "@default";

    public static final Collection<String> INLINE_ANNOTATIONS = Set.of(
            PUBLIC, PROTECTED, PRIVATE,
            FINAL, STATIC,
            SYNCHRONIZED, VOLATILE,
            STRICT, VARARGS, DEFAULT
    );

    public static final String THROWS_PREFIX = "@throws";
    public static final String EXTENDS_PREFIX = "@extends";
    public static final String IMPLEMENTS_PREFIX = "@implements";
    public static final String GENERIC_PREFIX = "@Parameterized";
    public static final String GENERIC_RESULT_PREFIX = "@ParameterizedResult";

    public static final String TARGET = "@target";

    public static Collection<String> modifiersToAnnotations(final int mods) {
        final var list = new ArrayList<String>();

        if (Modifier.isPrivate(mods))
            list.add(PRIVATE);
        else if (Modifier.isProtected(mods))
            list.add(PROTECTED);
        // functions are public-by-default

        // make private-default for package-private methods
        if (!Modifier.isPrivate(mods) && !Modifier.isProtected(mods) && !Modifier.isPublic(mods))
            list.add(PRIVATE);

        if (Modifier.isStatic(mods))
            list.add(STATIC);

        if (Modifier.isFinal(mods))
            list.add(FINAL);

        if (Modifier.isVolatile(mods))
            list.add(VOLATILE);

        if (Modifier.isSynchronized(mods))
            list.add(SYNCHRONIZED);

        if (Modifier.isStrict(mods))
            list.add(STRICT);

        if ((VARARGS_FLAG & mods) != 0)
            list.add(VARARGS);

        return List.copyOf(list);
    }

    public static String mkGeneric(final String generics) {
        return GENERIC_PREFIX + "([\"" + generics + "\"])";
    }

    public static String mkGeneric(final Collection<String> generics) {
        return GENERIC_PREFIX + "([\"" + String.join("\", \"", generics) + "\"])";
    }

    public static String mkGenericResult(final String generics) {
        return GENERIC_RESULT_PREFIX + "([\"" + generics + "\"])";
    }

    public static String mkGenericResult(final Collection<String> generics) {
        return GENERIC_RESULT_PREFIX + "([\"" + String.join("\", \"", generics) + "\"])";
    }

    public static String mkThrows(final Type[] exceptions) {
        final var sj = new StringJoiner("\", \"", "([\"", "\"])");
        for (final var e : exceptions)
            sj.add(e.getTypeName());

        return THROWS_PREFIX + sj;
    }

    public static String mkExtends(final Type parent) {
        return EXTENDS_PREFIX + "(\"" + parent.getTypeName() + "\")";
    }

    public static String mkImplements(final Type parentInterface) {
        return IMPLEMENTS_PREFIX + "(\"" + parentInterface.getTypeName() + "\")";
    }
}
