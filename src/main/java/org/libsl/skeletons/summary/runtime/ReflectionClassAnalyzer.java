package org.libsl.skeletons.summary.runtime;

import org.libsl.skeletons.sources.bytecode.BytecodeLoader;
import org.libsl.skeletons.sources.bytecode.ResourceClassLoader;
import org.libsl.skeletons.summary.Annotations;
import org.libsl.skeletons.summary.ClassSummary;
import org.libsl.skeletons.summary.ClassSummaryProducer;
import org.libsl.skeletons.summary.MethodSummary;
import org.libsl.skeletons.summary.bytecode.ParameterNameMiner;
import org.libsl.skeletons.util.ReflectionUtils;
import org.objectweb.asm.ClassReader;

import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

public final class ReflectionClassAnalyzer implements ClassSummaryProducer {
    private final Class<?> source;
    private final ClassSummary summary;
    private final boolean includeInheritedMethods;
    private final BytecodeLoader bytecodeLoader = new ResourceClassLoader();
    private final Map<String, ClassReader> classReaderCache = new HashMap<>();

    public ReflectionClassAnalyzer(final Class<?> source,
                                   final boolean includeInheritedMethods) {
        this.source = source;
        this.summary = new ClassSummary(
                source.getSimpleName(),
                source.getPackageName(),
                source.getTypeName(),
                Modifier.isAbstract(source.getModifiers()),
                source.isInterface()
        );
        this.includeInheritedMethods = includeInheritedMethods;

        ReflectionUtils.suppressIllegalAccessWarning();
    }

    private ClassReader getClassFromBytecode(final String name) {
        return classReaderCache.computeIfAbsent(name,
                n -> new ClassReader(bytecodeLoader.loadBytecodeFor(n)));
    }

    private void collectParameters(final MethodSummary method,
                                   final String[] parameterNames,
                                   final Class<?>[] parameterTypes) {
        if (parameterNames.length != parameterTypes.length)
            throw new AssertionError("Mismatching parameter sizes !!!");

        for (int i = 0; i < parameterNames.length; i++) {
            final var param = parameterNames[i];
            final var type = parameterTypes[i];

            // parse the type of the parameter
            final var paramSummary = TypeSplitter.split(type);

            // create a new "variable"
            method.addParameter(param, paramSummary.simpleType);
        }
    }

    private void collectImportReferences() {
        new ImportCollector(source, summary.imports, includeInheritedMethods)
                .findImports(
                        ElementClassifier::isSuitableConstructor,
                        ElementClassifier::isSuitablePublicMethod
                );
    }

    private void collectConstructorsInfo() {
        final var methodName = source.getSimpleName();

        // constructors
        for (var c : source.getDeclaredConstructors()) {
            if (!ElementClassifier.isSuitableConstructor(c))
                continue;

            final var signature = MethodSummary.getSignature(methodName, c.getParameterTypes());
            final var methodSummary = summary.addConstructor(signature);
            final var declaringClass = c.getDeclaringClass();

            // set the origin
            methodSummary.originClassName =
                    declaringClass != source
                            ? declaringClass.getCanonicalName()
                            : null;

            // throwable checked exceptions
            final var exceptions = c.getExceptionTypes();
            if (exceptions.length != 0) {
                final var filteredAnn = Annotations.mkThrows(exceptions);
                methodSummary.annotations.add(filteredAnn);
            }

            // special modifiers
            methodSummary.annotations.addAll(Annotations.modifiersToAnnotations(c.getModifiers()));

            // parameters
            final var jreDescriptor = ReflectionUtils.getDescriptor(c);
            final var parameterNames = getParameterNames(
                    declaringClass, ParameterNameMiner.CONSTRUCTOR_NAME, jreDescriptor, c.getParameters());

            collectParameters(methodSummary, parameterNames, c.getParameterTypes());
        }
    }

    private String[] getParameterNames(final Class<?> clazz,
                                       final String methodName,
                                       final String jreDescriptor,
                                       final Parameter[] parameters) {
        final var result = new String[parameters.length];
        final var isIndependent = !clazz.isMemberClass() || Modifier.isStatic(clazz.getModifiers());

        // backup option
        for (int i = 0; i < parameters.length; i++)
            result[i] = parameters[i].getName();

        // fetch actual names from bytecode
        ParameterNameMiner.mineParameterNames(
                getClassFromBytecode(clazz.getTypeName()),
                isIndependent,
                methodName,
                jreDescriptor,
                result);

        return result;
    }

    private void collectMethodsInfo() {
        final var methodList =
                includeInheritedMethods && source != Object.class
                        ? ReflectionUtils.getMethodsInherited(source, ElementClassifier::isSuitablePublicMethod)
                        : ReflectionUtils.getMethodsDirect(source, ElementClassifier::isSuitablePublicMethodWithObject);

        // public methods (including static ones)
        for (var m : methodList) {
            final var methodName = m.getName();
            final var mods = m.getModifiers();
            final var isStatic = Modifier.isStatic(mods);
            final var signature = MethodSummary.getSignature(methodName, m.getParameterTypes());
            final var declaringClass = m.getDeclaringClass();

            final var retTypeSummary = TypeSplitter.split(m.getReturnType());

            if (isStatic && summary.staticMethods.containsKey(signature)
                    || summary.instanceMethods.containsKey(signature))
                throw new RuntimeException("duplicate method: " + m);

            final MethodSummary methodSummary;
            if (isStatic)
                methodSummary = summary.addStaticMethod(methodName, signature, retTypeSummary.simpleType);
            else
                methodSummary = summary.addInstanceMethod(methodName, signature, retTypeSummary.simpleType);

            // set the origin info
            methodSummary.originClassName =
                    declaringClass != source
                            ? declaringClass.getCanonicalName()
                            : null;

            // throwable checked exceptions
            final var exceptions = m.getExceptionTypes();
            if (exceptions.length != 0) {
                final var filteredAnn = Annotations.mkThrows(exceptions);
                methodSummary.annotations.add(filteredAnn);
            }

            // special modifiers
            methodSummary.annotations.addAll(Annotations.modifiersToAnnotations(m.getModifiers()));
            if (m.isDefault())
                methodSummary.annotations.add(Annotations.DEFAULT);

            // parameters
            final var jreDescriptor = ReflectionUtils.getDescriptor(m);
            final var parameterNames = getParameterNames(
                    declaringClass, methodName, jreDescriptor, m.getParameters());

            collectParameters(methodSummary, parameterNames, m.getParameterTypes());
        }
    }

    private void collectSpecialConstants() {
        new SpecialConstantCollector(source, summary, false)
                .findConstants(ElementClassifier::isSuitablePublicField);
    }

    @Override
    public ClassSummary collectInfo() {
        // special constants
        collectSpecialConstants();

        // parent info
        final var parent = source.getSuperclass();
        if (parent != null && parent != Object.class) {
            final var filteredAnn = Annotations.mkExtends(parent);
            summary.annotations.add(filteredAnn);
        }

        // interfaces info
        final var parentInterfaces = source.getInterfaces();
        for (var i : parentInterfaces) {
            final var filteredAnn = Annotations.mkImplements(i);
            summary.annotations.add(filteredAnn);
        }

        // special info
        summary.annotations.addAll(Annotations.modifiersToAnnotations(source.getModifiers()));

        // method information
        collectImportReferences();
        collectConstructorsInfo();
        collectMethodsInfo();

        return summary;
    }
}
