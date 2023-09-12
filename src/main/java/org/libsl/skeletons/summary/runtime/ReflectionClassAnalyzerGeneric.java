package org.libsl.skeletons.summary.runtime;

import org.libsl.skeletons.sources.bytecode.BytecodeLoader;
import org.libsl.skeletons.sources.bytecode.ResourceClassLoader;
import org.libsl.skeletons.summary.Annotations;
import org.libsl.skeletons.summary.ClassSummary;
import org.libsl.skeletons.summary.ClassSummaryProducer;
import org.libsl.skeletons.summary.MethodSummary;
import org.libsl.skeletons.summary.bytecode.ParameterNameMiner;
import org.libsl.skeletons.util.ReflectionUtils;
import org.objectweb.asm.*;
import sun.misc.Unsafe;

import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ReflectionClassAnalyzerGeneric implements ClassSummaryProducer {
    private final Class<?> source;
    private final ClassSummary summary;
    private final boolean includeInheritedMethods;
    private final BytecodeLoader bytecodeLoader = new ResourceClassLoader();
    private final Map<String, ClassReader> classReaderCache = new HashMap<>();

    public ReflectionClassAnalyzerGeneric(final Class<?> source,
                                          final boolean includeInheritedMethods) {
        this.source = source;
        this.summary = new ClassSummary(source.getSimpleName(), source.getTypeName());
        this.includeInheritedMethods = includeInheritedMethods;

        suppressIllegalAccessWarning();
    }

    private static void suppressIllegalAccessWarning() {
        try {
            final var theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            final var u = (Unsafe) theUnsafe.get(null);

            final var cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            final var logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
        } catch (Exception ignore) {
        }
    }

    private ClassReader getClassFromBytecode(final String name) {
        return classReaderCache.computeIfAbsent(name,
                n -> new ClassReader(bytecodeLoader.loadBytecodeFor(n)));
    }

    private String genericsFilter(final String in) {
        final var regex = Pattern.quote(source.getCanonicalName());
        return in.replaceAll(regex, source.getSimpleName());
    }

    private Collection<String> genericsFilter(final Collection<String> in) {
        return in.stream()
                .map(this::genericsFilter)
                .collect(Collectors.toUnmodifiableList());
    }

    private void collectParameters(final MethodSummary method,
                                   final String[] parameterNames,
                                   final Type[] genericParameterTypes) {
        if (parameterNames.length != genericParameterTypes.length)
            throw new AssertionError("Mismatching parameter sizes !!!");

        for (int i = 0; i < parameterNames.length; i++) {
            final var param = parameterNames[i];
            final var type = genericParameterTypes[i];

            // parse the type of the parameter
            final var paramSummary = GenericTypeSplitter.split(type);

            // create a new "variable"
            final var paramVar = method.addParameter(param, paramSummary.simpleType);

            // add annotations
            if (paramSummary.hasTypeArguments) {
                final var filteredStr = genericsFilter(paramSummary.typeArgs);
                paramVar.annotations.add(Annotations.mkGeneric(filteredStr));
            }
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

            // generics in declaration
            final var declarationParameters = c.getTypeParameters();
            if (declarationParameters.length != 0) {
                final var filteredStr = genericsFilter(declarationParamsToString(declarationParameters));
                methodSummary.annotations.add(Annotations.mkGeneric(filteredStr));
            }

            // throwable checked exceptions
            final var exceptions = c.getGenericExceptionTypes();
            if (exceptions.length != 0) {
                final var filteredAnn = genericsFilter(Annotations.mkThrows(exceptions));
                methodSummary.annotations.add(filteredAnn);
            }

            // special modifiers
            methodSummary.annotations.addAll(Annotations.modifiersToAnnotations(c.getModifiers()));

            // parameters
            final var jreDescriptor = ReflectionUtils.getDescriptor(c);
            final var parameterNames = getParameterNames(
                    declaringClass, ParameterNameMiner.CONSTRUCTOR_NAME, jreDescriptor, c.getParameters());

            collectParameters(methodSummary, parameterNames, c.getGenericParameterTypes());
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

            final var retTypeSummary = GenericTypeSplitter.split(m.getGenericReturnType());

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

            // generics in declaration
            final var declarationParameters = m.getTypeParameters();
            if (declarationParameters.length != 0) {
                final var filteredStr = genericsFilter(declarationParamsToString(declarationParameters));
                methodSummary.annotations.add(Annotations.mkGeneric(filteredStr));
            }

            // generics in return type
            if (retTypeSummary.hasTypeArguments) {
                final var filteredStr = genericsFilter(retTypeSummary.typeArgs);
                methodSummary.annotations.add(Annotations.mkGenericResult(filteredStr));
            }

            // throwable checked exceptions
            final var exceptions = m.getGenericExceptionTypes();
            if (exceptions.length != 0) {
                final var filteredAnn = genericsFilter(Annotations.mkThrows(exceptions));
                methodSummary.annotations.add(filteredAnn);
            }

            // special modifiers
            methodSummary.annotations.addAll(Annotations.modifiersToAnnotations(m.getModifiers()));

            // parameters
            final var jreDescriptor = ReflectionUtils.getDescriptor(m);
            final var parameterNames = getParameterNames(declaringClass, methodName, jreDescriptor, m.getParameters());
            collectParameters(methodSummary, parameterNames, m.getGenericParameterTypes());
        }
    }

    private static Collection<String> declarationParamsToString(final TypeVariable<?>[] params) {
        final var result = new ArrayList<String>();

        for (var tv : params) {
            final var name = tv.getName();

            // correct bounds extraction
            final var ssj = new StringJoiner(" & ", " extends ", "");
            var actualBounds = 0;
            for (var t : tv.getBounds())
                if (t != Object.class) {
                    ssj.add(t.getTypeName());
                    ++actualBounds;
                }

            final var suffix = actualBounds == 0 ? "" : ssj.toString();
            result.add(name + suffix);
        }

        return Collections.unmodifiableCollection(result);
    }

    private void collectTypeVariables() {
        new TypeVariableCollector(source, summary.allGenericTypeVariables)
                .collectTypeParameters(
                        ElementClassifier::isSuitableConstructor,
                        ElementClassifier::isSuitablePublicMethod
                );
    }

    private void collectSpecialConstants() {
        new SpecialConstantCollector(source, summary, true)
                .findConstants(ElementClassifier::isSuitablePublicField);
    }

    @Override
    public ClassSummary collectInfo() {
        // incoming generic parameters
        final var declarationParameters = source.getTypeParameters();
        if (declarationParameters.length != 0) {
            final var filteredStr = genericsFilter(declarationParamsToString(declarationParameters));
            summary.annotations.add(Annotations.mkGeneric(filteredStr));
        }

        // special constants
        collectSpecialConstants();

        // parent info
        final var parent = source.getGenericSuperclass();
        if (parent != null && parent != Object.class) {
            final var filteredAnn = genericsFilter(Annotations.mkExtends(parent));
            summary.annotations.add(filteredAnn);
        }

        // interfaces info
        final var parentInterfaces = source.getGenericInterfaces();
        for (var i : parentInterfaces) {
            final var filteredAnn = genericsFilter(Annotations.mkImplements(i));
            summary.annotations.add(filteredAnn);
        }

        // special info
        summary.annotations.addAll(Annotations.modifiersToAnnotations(source.getModifiers()));

        // method information
        collectImportReferences();
        collectTypeVariables();
        collectConstructorsInfo();
        collectMethodsInfo();

        return summary;
    }
}
