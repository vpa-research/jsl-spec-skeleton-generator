package org.libsl.skeletons.summary.bytecode;

import org.libsl.skeletons.sources.bytecode.BytecodeLoader;
import org.libsl.skeletons.summary.ClassSummary;
import org.libsl.skeletons.summary.ClassSummaryProducer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class BytecodeClassAnalyzer implements ClassSummaryProducer {
    private final BytecodeLoader loader;
    private final Map<String, ClassReader> classCache = new HashMap<>();
    private final ClassReader source;
    private final ClassSummary summary;

    public BytecodeClassAnalyzer(final BytecodeLoader bytecodeLoader) {
        this.loader = bytecodeLoader;
        this.source = getClass("?");

        final var canonicalName = source.getClassName().replace('/', '.');
        final var simpleName = getSimpleName(canonicalName);
        final var packageName = getPackageName(canonicalName, simpleName);

        final var flags = source.getAccess();
        this.summary = new ClassSummary(
                simpleName,
                packageName,
                canonicalName,
                hasFlag(flags, Opcodes.ACC_ABSTRACT),
                hasFlag(flags, Opcodes.ACC_INTERFACE)
        );
    }

    private static boolean hasFlag(final int flags, final int flag) {
        return (flags & flag) == flag;
    }

    private static String getSimpleName(final String canonicalName) {
        final var segments = canonicalName.split("\\.");

        return Arrays.stream(segments)
                .dropWhile(s -> Character.isLowerCase(s.charAt(0)))
                .collect(Collectors.joining("."));
    }

    private String getPackageName(final String canonicalName, final String simpleName) {
        final var nameIndex = canonicalName.indexOf(simpleName);
        return canonicalName.substring(0, nameIndex - 1);
    }

    private ClassReader getClass(final String name) {
        return classCache.computeIfAbsent(name,
                n -> new ClassReader(loader.loadBytecodeFor(n)));
    }

    @Override
    public ClassSummary collectInfo() {
        return summary;
    }
}
