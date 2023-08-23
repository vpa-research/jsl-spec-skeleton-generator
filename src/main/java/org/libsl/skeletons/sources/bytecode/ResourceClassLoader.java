package org.libsl.skeletons.sources.bytecode;

import java.io.IOException;

public final class ResourceClassLoader implements BytecodeLoader {
    private final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    @Override
    public byte[] loadBytecodeFor(final String canonicalClassName) {
        final var path = canonicalClassName.replace('.', '/').concat(".class");

        try (final var res = classLoader.getResourceAsStream(path)) {
            assert res != null;

            return res.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
