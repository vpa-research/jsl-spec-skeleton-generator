package org.libsl.skeletons.summary;

import java.util.Set;

public final class KeywordFilter {
    private static final Set<String> KEYWORDS = Set.of(new String[]{
            "libsl", "library", "import", "include",
            "type", "typealias",
            "define", "action",
            "annotation",
            "automaton", "concept",
            "fun", "proc",
            "is", "has", "as",
            "this", "self", "target",
    });

    private KeywordFilter() {
    }

    public static String renameIfKeyword(final String name) {
        return KEYWORDS.contains(name)
                ? "_" + name
                : name;
    }
}
