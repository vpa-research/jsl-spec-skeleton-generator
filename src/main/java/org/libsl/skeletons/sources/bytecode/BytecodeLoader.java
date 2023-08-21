package org.libsl.skeletons.sources.bytecode;

public interface BytecodeLoader {
    byte[] loadBytecodeFor(final String canonicalClassName);
}
