package org.libsl.skeletons.summary.bytecode;

import org.objectweb.asm.*;

import java.util.*;
import java.util.stream.Collectors;

public final class ParameterNameMiner {
    public static final String CONSTRUCTOR_NAME = "<init>";

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

        public List<String> getMinedParameterNames(final int expectedParameterCount) {
            return Collections.unmodifiableList(methodMiner.getMinedParameterNames(expectedParameterCount));
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
        private final List<String> minedParameters = new ArrayList<>();
        private final Map<Integer, String> minedLocalVariables = new HashMap<>();

        private MethodMiner() {
            super(Opcodes.ASM9);
        }

        @Override
        public void visitParameter(final String name, final int access) {
            minedParameters.add(name);
        }

        @Override
        public void visitLocalVariable(final String name,
                                       final String descriptor,
                                       final String signature,
                                       final Label start,
                                       final Label end,
                                       final int index) {
            minedLocalVariables.put(index, name);
        }

        public List<String> getMinedParameterNames(final int expectedParameterCount) {
            final var res = new String[expectedParameterCount];

            // first estimation from local variables
            for (var e : minedLocalVariables.entrySet()) {
                final var index = e.getKey();
                final var name = e.getValue();

                if (index < expectedParameterCount)
                    res[index] = name;
            }

            // second (accurate) estimation from actual parameters
            final var pCount = Math.min(minedParameters.size(), res.length);
            for (int i = 0; i < pCount; i++)
                res[i] = minedParameters.get(i);

            return Arrays.asList(res);
        }
    }

    public static void mineParameterNames(final ClassReader clazz,
                                          final boolean isIndependentClass,
                                          final String methodName,
                                          final String methodDescriptor,
                                          final String[] preparedParameters) {
        // prepare
        final var expectedParameterCount = preparedParameters.length + 1;
        final var classVisitor = new ClassMiner(methodName, methodDescriptor);
        final var isConstructor = CONSTRUCTOR_NAME.equals(methodName);
        final var offsetThis = isConstructor && !isIndependentClass ? 0 : 1;

        // process
        clazz.accept(classVisitor, 0);
        final var minedParameterNames = classVisitor.getMinedParameterNames(expectedParameterCount);
        final var minedParameterNamesMaxIndex = minedParameterNames.size();
        final var minedParameterCount = Math.min(minedParameterNamesMaxIndex, preparedParameters.length);

        // reconstruct names
        final var baseIndex = Math.max(minedParameterNames.indexOf("this") + offsetThis, 0);
        for (int i = 0; i < minedParameterCount; i++) {
            final var index = baseIndex + i;
            if (index < minedParameterNamesMaxIndex) {
                final var suggestedName = minedParameterNames.get(index);
                if (suggestedName != null)
                    preparedParameters[i] = suggestedName;
            }
        }
    }
}
