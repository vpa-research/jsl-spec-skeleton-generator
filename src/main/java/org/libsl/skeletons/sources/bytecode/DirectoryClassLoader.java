package org.libsl.skeletons.sources.bytecode;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

public final class DirectoryClassLoader implements BytecodeLoader {
    private final Collection<Path> rootDirs;

    public DirectoryClassLoader(final Collection<Path> rootDirCollection) {
        Objects.requireNonNull(rootDirCollection);
        this.rootDirs = rootDirCollection;
    }

    @Override
    public byte[] loadBytecodeFor(final String canonicalClassName) {
        throw new RuntimeException("TODO");
    }
}
