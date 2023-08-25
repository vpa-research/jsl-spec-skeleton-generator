package org.libsl.skeletons.summary.bytecode;

import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ParameterNameMiner {
    private ParameterNameMiner() {
    }

    private static final class ClassMiner extends ClassVisitor {
        private final String expectedMethod;
        private final String expectedDescriptor;
        private final MethodMiner methodMiner = new MethodMiner();

        private ClassMiner(final String methodName, final String methodDescriptor) {
            super(Opcodes.ASM9);
            this.expectedMethod = Objects.requireNonNull(methodName);
            this.expectedDescriptor = Objects.requireNonNull(methodDescriptor);
        }

        public List<String> getMinedParameterNames() {
            return Collections.unmodifiableList(methodMiner.minedParameterNames);
        }

        @Override
        public MethodVisitor visitMethod(final int access,
                                         final String name,
                                         final String descriptor,
                                         final String signature,
                                         final String[] exceptions) {
            return expectedMethod.equals(name) && expectedDescriptor.equals(descriptor)
                    ? methodMiner
                    : null;
        }
    }

    private static final class MethodMiner extends MethodVisitor {
        public final List<String> minedParameterNames = new ArrayList<>();

        private MethodMiner() {
            super(Opcodes.ASM9);
        }

        @Override
        public void visitParameter(final String name, final int access) {
            minedParameterNames.add(name);
        }

        @Override
        public void visitLocalVariable(final String name,
                                       final String descriptor,
                                       final String signature,
                                       final Label start,
                                       final Label end,
                                       final int index) {
            minedParameterNames.add(name);
        }
    }

    public static void mineParameterNames(final ClassReader clazz,
                                          final String methodName,
                                          final String methodDescriptor,
                                          final String[] preparedParameters) {
        // prepare
        final var classVisitor = new ClassMiner(methodName, methodDescriptor);

        // process
        clazz.accept(classVisitor, 0);

        // reconstruct names
        final var minedParameterNames = classVisitor.getMinedParameterNames();
        final var baseIndex = Math.max(minedParameterNames.indexOf("this") + 1, 0);
        final var minedParameterCount = Math.min(minedParameterNames.size(), preparedParameters.length);
        for (int i = 0; i < minedParameterCount; i++)
            preparedParameters[i] = minedParameterNames.get(baseIndex + i);
    }
}