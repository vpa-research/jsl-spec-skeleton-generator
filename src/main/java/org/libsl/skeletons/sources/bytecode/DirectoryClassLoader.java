package org.libsl.skeletons.sources.bytecode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class DirectoryClassLoader implements BytecodeLoader {
    private final Collection<Path> rootDirs;
    private final Map<String, Path> classLocations = new HashMap<>();

    public DirectoryClassLoader(final Collection<Path> rootDirCollection) {
        Objects.requireNonNull(rootDirCollection);
        this.rootDirs = rootDirCollection;
    }

    private Path getClassLocation(final String className) {
        return classLocations.computeIfAbsent(className, n -> {
            final var relativePath = Path.of(n.replace('.', '/').concat(".class"));

            for (var dir : rootDirs) {
                final var path = dir.resolve(relativePath);
                if (Files.exists(path))
                    return path;
            }

            throw new RuntimeException("Unable to find class " + n + " within the specified set of directories");
        });
    }

    @Override
    public byte[] loadBytecodeFor(final String canonicalClassName) {
        throw new RuntimeException("TODO");
    }
}
