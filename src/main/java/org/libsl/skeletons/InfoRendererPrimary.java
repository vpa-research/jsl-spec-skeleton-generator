package org.libsl.skeletons;

import org.libsl.skeletons.util.PrettyPrinter;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static org.libsl.skeletons.Annotations.*;
import static org.libsl.skeletons.util.PrettyPrinter.TAB;

final class InfoRendererPrimary extends AbstractInfoRenderer {
    private static final String METHOD_NAME_PREFIX = "*.";
    private static final String ORIGIN_URL_PREFIX = "https://github.com/openjdk/jdk11/blob/master/src/java.base/share/classes";

    private final ClassSummary summary;
    private final String automatonName;

    public InfoRendererPrimary(final ClassSummary summary, final PrettyPrinter out) {
        super(out);
        this.summary = summary;
        this.automatonName = summary.simpleName + "Automaton";
    }

    private void renderMethod(final MethodSummary method) {
        out.addln();
        out.addln();

        // annotations
        for (var ann : method.annotations)
            if (!INLINE_ANNOTATIONS.contains(ann))
                out.addln(ann);

        for (var ann : method.annotations)
            if (INLINE_ANNOTATIONS.contains(ann))
                out.add(ann).add(" ");

        // keyword + name
        final var fullMethodName = METHOD_NAME_PREFIX + method.simpleName;
        out.add(method.isConstructor ? CONSTRUCTOR : FUNCTION).add(" ").add(fullMethodName);

        // parameters
        out.add(" (");

        // artificial reference to object instance
        if (!summary.isStaticMethod(method.signature)) {
            out.add(TARGET).add(" self: ").add(summary.simpleName);

            if (!method.parameters.isEmpty())
                out.add(", ");
        }

        var pCounter = method.parameters.size();
        for (var p : method.parameters.values()) {
            p.annotations.forEach(a -> out.add(a).add(" "));

            out.add(p.name).add(": ").add(p.simpleType);

            if (pCounter-- > 1)
                out.add(", ");
        }
        out.add(")");

        // return value type
        if (!method.isConstructor)
            out.add(": ").addln(method.returnType);
        else
            out.addln();

        // method body
        out.addln("{");
        out.beginBlock();

        out.addln(method.body);

        out.endBlock();
        out.addln("}");
    }

    private void renderMethodBatch(final String batchTitle, final Collection<MethodSummary> batch) {
        out.addln();
        out.add("// ").add(batchTitle);

        batch.forEach(this::renderMethod);

        out.addln();
    }

    private int renderSpecialConstants(final Predicate<VariableSummary> scFilter) {
        var rendered = 0;
        for (VariableSummary sc : summary.specialConstants.values()) {
            if (!scFilter.test(sc))
                continue;
            else
                rendered += 1;

            // inline annotations firs, then others
            for (var ann : sc.annotations)
                if (INLINE_ANNOTATIONS.contains(ann))
                    out.add(ann).add(" ");

            for (var ann : sc.annotations)
                if (!INLINE_ANNOTATIONS.contains(ann))
                    out.add(ann).add(" ");

            // usual automaton variable declaration
            out.add("var ").add(sc.name).add(": ").add(sc.simpleType).add(" = ").add(sc.value).addln(";");
        }

        return rendered;
    }

    private void renderSpecialConstants() {
        // private and protected special fields first
        final int privateCount = renderSpecialConstants(sc -> !sc.annotations.contains(PUBLIC));
        final int publicCount = summary.specialConstants.size() - privateCount;

        // blank separator
        if (privateCount != 0 && publicCount != 0)
            out.addln();

        // other (public) ones
        renderSpecialConstants(sc -> sc.annotations.contains(PUBLIC));
    }

    private void renderClassTypeAnnotations() {
        for (var ann : summary.annotations)
            if (!INLINE_ANNOTATIONS.contains(ann))
                out.addln(ann);

        // inline annotations
        out.add(PUBLIC).add(" ");
        for (var ann : summary.annotations)
            if (INLINE_ANNOTATIONS.contains(ann))
                out.add(ann).add(" ");
    }

    private void renderAutomatonHeader() {
        out.addln();
        out.addln("// automata");
        out.addln();

        out.add("automaton ").addln(automatonName);
        out.addln("(");
        out.addln(")");
        out.add(": ").addln(summary.simpleName);
    }

    private void renderSemanticTypes() {
        out.addln();
        out.addln("// local semantic types");
        out.addln();

        renderClassTypeAnnotations();

        out.add("type ").add(summary.simpleName).addln();
        out.beginBlock();
        out.addln("is %s", summary.typeName);
        out.addln("for %s", "Object");
        out.endBlock();
        out.addln("{");
        out.beginBlock();

        renderSpecialConstants();

        out.endBlock();
        out.addln("}");
        out.addln();
    }

    private void renderImports() {
        out.add(IMPORT).addln(" java.common;");

        for (var ref : summary.imports) {
            final var path = ref.replace('.', '/');
            out.add(IMPORT).add(" ").add(path).addln(";");
        }

        out.addln();
    }

    private void renderPreamble() {
        out.addln("libsl \"1.1.0\";");
        out.addln();
        out.addln("library std");
        out.beginBlock();
        out.addln("version \"11\"");
        out.addln("language \"Java\"");
        out.addln(
                "url \"%s/%s.java\";",
                ORIGIN_URL_PREFIX,
                summary.typeName.replace('.', '/'));
        out.endBlock();
        out.addln();
        out.addln("// imports");
        out.addln();

        renderImports();
    }

    private void renderStatesAndShifts() {
        out.addln("// states and shifts");
        out.addln();

        if (summary.constructors.isEmpty() && summary.staticMethods.isEmpty()) {
            out.addln("initstate Initialized;");
        } else {
            out.addln("initstate Allocated;");
            out.addln("state Initialized;");
            out.addln();

            out.addln("shift %s -> %s by [", "Allocated", "Initialized");
            out.beginBlock();

            if (!summary.constructors.isEmpty())
                renderShiftCollection(
                        "constructors",
                        summary.constructors.values(),
                        this::fixInstanceSignature);

            if (!summary.staticMethods.isEmpty())
                renderShiftCollection(
                        "static operations",
                        summary.staticMethods.values(),
                        UnaryOperator.identity());

            out.endBlock();
            out.addln("];");
        }
        out.addln();

        out.addln("shift %s -> %s by [", "Initialized", "self");
        out.beginBlock();

        renderShiftCollection(
                "instance methods",
                summary.instanceMethods.values(),
                this::fixInstanceSignature);

        out.endBlock();
        out.addln("];");
    }

    private String fixInstanceSignature(final String signature) {
        // check for brackets first
        final var bracketIndex = signature.indexOf('(');
        if (bracketIndex > 0) {
            final var spaceIndex = signature.indexOf(' ');
            final var methodNameLength = spaceIndex > 0
                    ? spaceIndex
                    : bracketIndex;

            final var methodName = signature.substring(0, methodNameLength);

            // remove brackets if there are no other methods with the same name
            if (!hasOverloads(methodName))
                return methodName;

            return methodName
                    + " (" + summary.simpleName + ", "
                    + signature.substring(bracketIndex + 1);
        }

        // no brackets - looking for overloads
        final var hasOverloads = hasOverloads(signature);

        // add instance reference if there are overloads
        return hasOverloads
                ? signature + " (" + summary.simpleName + ")"
                : signature;
    }

    private boolean hasOverloads(final String methodName) {
        var counter = 0;

        for (var m : summary.constructors.values())
            if (m.simpleName.equals(methodName)) {
                counter += 1;

                if (counter > 1)
                    return true;
            }

        if (counter == 0)
            for (var m : summary.instanceMethods.values())
                if (m.simpleName.equals(methodName)) {
                    counter += 1;

                    if (counter > 1)
                        return true;
                }

        return false;
    }

    private void renderShiftCollection(final String section,
                                       final Collection<MethodSummary> methods,
                                       final UnaryOperator<String> sigModifier) {
        out.add("// ").addln(section);
        for (var m : methods)
            out.addln("%s,", sigModifier.apply(m.signature));
    }

    public void render() {
        renderPreamble();

        renderSemanticTypes();

        renderAutomatonHeader();
        out.addln("{");
        out.beginBlock();

        renderStatesAndShifts();

        renderMethodBatch("internal variables", List.of());
        renderMethodBatch("utilities", List.of());
        renderMethodBatch("constructors", summary.constructors.values());
        renderMethodBatch("static methods", summary.staticMethods.values());
        renderMethodBatch("methods", summary.instanceMethods.values());

        out.endBlock();
        out.addln("}");
    }
}
