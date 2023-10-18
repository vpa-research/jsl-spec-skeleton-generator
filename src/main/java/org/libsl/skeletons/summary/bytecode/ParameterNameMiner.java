package org.libsl.skeletons.summary.bytecode;

import org.objectweb.asm.*;

import java.util.*;

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
        private final Map<Integer, String> minedLocalVariables = new TreeMap<>();
        private final Map<String, Label> localVariableLabelMap = new IdentityHashMap<>();

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
            localVariableLabelMap.put(name, start);
            minedLocalVariables.put(index, name);
        }

        public List<String> getMinedParameterNames(final int expectedParameterCount) {
            final var res = new ArrayList<String>(expectedParameterCount);

            // first estimate from actual parameters if any
            final var paramsIter = minedParameters.iterator();
            while (res.size() < expectedParameterCount && paramsIter.hasNext())
                res.add(paramsIter.next());

            // then pull from local variables
            final var localsIter = minedLocalVariables.values().iterator();
            while (res.size() < expectedParameterCount && localsIter.hasNext()) {
                final var variableName = localsIter.next();

                if (!res.contains(variableName) && isParameter(variableName))
                    res.add(variableName);
            }

            // seal
            return List.copyOf(res);
        }

        private boolean isParameter(final String variableName) {
            try {
                return localVariableLabelMap.get(variableName).getOffset() == 0;
            } catch (IllegalStateException e) {
                return true;
            }
        }
    }

    public static void mineParameterNames(final ClassReader clazz,
                                          final boolean isIndependentClass,
                                          final String methodName,
                                          final String methodDescriptor,
                                          final String[] preparedParameters) {
        // prepare
        final var expectedParameterCount = preparedParameters.length + 1 /* "this" */;
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
