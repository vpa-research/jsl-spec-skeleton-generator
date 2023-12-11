package org.libsl.skeletons.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Java-specific string utility classes.
 */
public final class StringUtils {
    private static final List<Pair<String, String>> ESCAPE_PAIRS = new ArrayList<>();
    private static final Predicate<String> PATTERN_IDENTIFIER = Pattern.compile("[A-Za-z0-9_]+").asMatchPredicate();

    static {
        esc_map("\\", "\\\\");
        esc_map("\n", "\\n");
        esc_map("\r", "\\r");
        esc_map("\t", "\\t");
        esc_map("\"", "\\\"");
    }

    private static void esc_map(final String from, final String to) {
        ESCAPE_PAIRS.add(new Pair<>(from, to));
    }

    private StringUtils() {
    }

    public static String escape(String text) {
        final var iter = ESCAPE_PAIRS.listIterator();

        while (iter.hasNext()) {
            final var pair = iter.next();
            text = text.replace(pair.left, pair.right);
        }

        return text;
    }

    public static String unescape(String text) {
        final var iter = ESCAPE_PAIRS.listIterator(ESCAPE_PAIRS.size());

        while (iter.hasPrevious()) {
            final var pair = iter.previous();
            text = text.replace(pair.right, pair.left);
        }

        return text;
    }

    public static String prettyConstant(final Object value) {
        if (value == null)
            return "null";

        final var cls = value.getClass();
        final var valueStr = value.toString();

        if (cls == double.class || cls == Double.class)
            return valueStr + "d";

        if (cls == long.class || cls == Long.class)
            return valueStr + "L";

        if (cls == float.class || cls == Float.class)
            return valueStr + "f";

        if (cls == String.class)
            return "\"" + valueStr + "\"";

        return valueStr;
    }

    public static String removeBackticks(final String s) {
        return s.startsWith("`")
                ? s.substring(1, s.length() - 2)
                : s;
    }

    public static String addBackticksIfNecessary(final String s) {
        return !PATTERN_IDENTIFIER.test(s)
                ? "`" + s + "`"
                : s;
    }

    public static String getCompleteClassName(final String typeName) {
        return typeName
                .substring(typeName.lastIndexOf('.') + 1)
                .replace('$', '_');
    }

    public static String getCompleteClassName(final Type type) {
        return getCompleteClassName(type.getTypeName());
    }
}
