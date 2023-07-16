package org.libsl.skeletons;

import sun.misc.Unsafe;

import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Pattern;

final class JslClassSummaryConstructor {
    private final Class<?> source;
    private final ClassSummary summary;

    public JslClassSummaryConstructor(final Class<?> source) {
        this.source = source;
        this.summary = new ClassSummary(source.getSimpleName(), source.getTypeName());

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

    private String genericsFilter(final String in) {
        final var regex = Pattern.quote(source.getCanonicalName());
        return in.replaceAll(regex, source.getSimpleName());
    }

    private void collectParameters(final MethodSummary method,
                                   final Parameter[] parameters,
                                   final Type[] genericParameterTypes) {
        if (parameters.length != genericParameterTypes.length)
            throw new AssertionError("Mismatching parameter sizes !!!");

        for (int i = 0; i < parameters.length; i++) {
            final var param = parameters[i];
            final var type = genericParameterTypes[i];

            // parse the type of the parameter
            final var paramSummary = GenericTypeSplitter.split(type);

            // create a new "variable"
            final var paramVar = method.addParameter(param.getName(), paramSummary.simpleType);

            // add annotations
            if (paramSummary.hasTypeArguments) {
                final var filteredStr = genericsFilter(paramSummary.typeArgsAsString());
                paramVar.annotations.add(Annotations.mkGeneric(filteredStr));
            }
        }
    }

    private void collectImportReferences() {
        new ImportCollector(source, summary.imports)
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
            collectParameters(methodSummary, c.getParameters(), c.getGenericParameterTypes());
        }
    }

    private void collectMethodsInfo() {
        // public methods (including static ones)
        for (var m : source.getDeclaredMethods()) {
            final var mods = m.getModifiers();
            if (!ElementClassifier.isSuitablePublicMethod(m))
                continue;

            final var methodName = m.getName();
            final var isStatic = Modifier.isStatic(mods);
            final var signature = MethodSummary.getSignature(methodName, m.getParameterTypes());

            final var retTypeSummary = GenericTypeSplitter.split(m.getGenericReturnType());

            final MethodSummary methodSummary;
            if (isStatic)
                methodSummary = summary.addStaticMethod(methodName, signature, retTypeSummary.simpleType);
            else
                methodSummary = summary.addInstanceMethod(methodName, signature, retTypeSummary.simpleType);

            // generics in declaration
            final var declarationParameters = m.getTypeParameters();
            if (declarationParameters.length != 0) {
                final var filteredStr = genericsFilter(declarationParamsToString(declarationParameters));
                methodSummary.annotations.add(Annotations.mkGeneric(filteredStr));
            }

            // generics in return type
            if (retTypeSummary.hasTypeArguments) {
                final var filteredStr = genericsFilter(retTypeSummary.typeArgsAsString());
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
            collectParameters(methodSummary, m.getParameters(), m.getGenericParameterTypes());
        }
    }

    private static String declarationParamsToString(final TypeVariable<?>[] params) {
        final var sj = new StringJoiner(", ");
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
            sj.add(name + suffix);
        }

        return sj.toString();
    }

    private void collectTypeVariables() {
        new TypeVariableCollector(source, summary.allGenericTypeVariables)
                .collectTypeParameters(
                        ElementClassifier::isSuitableConstructor,
                        ElementClassifier::isSuitablePublicMethod
                );
    }

    private void collectSpecialConstants() {
        new SpecialConstantCollector(source, summary)
                .findConstants(ElementClassifier::isSuitablePublicField);
    }

    public ClassSummary collectInfo() {
        // incoming generic parameters
        final var declarationParameters = source.getTypeParameters();
        if (declarationParameters.length != 0) {
            final String filteredStr = genericsFilter(declarationParamsToString(declarationParameters));
            summary.annotations.add(Annotations.mkGeneric(filteredStr));
        }

        // special constants
        collectSpecialConstants();

        // parent info
        final var parent = source.getGenericSuperclass();
        if (parent != null && parent != Object.class) {
            final String filteredAnn = genericsFilter(Annotations.mkExtends(parent));
            summary.annotations.add(filteredAnn);
        }

        // interfaces info
        final var parentInterfaces = source.getGenericInterfaces();
        if (parentInterfaces.length != 0) {
            final var filteredAnn = genericsFilter(Annotations.mkImplements(parentInterfaces));
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
