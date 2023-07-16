package org.libsl.skeletons;

import org.libsl.skeletons.util.PrettyPrinter;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static org.libsl.skeletons.Annotations.*;
import static org.libsl.skeletons.util.PrettyPrinter.TAB;

final class InfoRendererIndirect extends AbstractInfoRenderer {
    private static final String COMPOUND_NAME_SEPARATOR = "#";
    private final ClassSummary summary;
    private final String automatonName;

    public InfoRendererIndirect(final ClassSummary summary, final PrettyPrinter out) {
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
        final var fullMethodName = summary.simpleName + COMPOUND_NAME_SEPARATOR + method.simpleName;
        out.add(method.isConstructor ? CONSTRUCTOR : FUNCTION).add(" `").add(fullMethodName).add("`");

        // parameters
        out.add(" (");

        // artificial reference to object instance
        if (!summary.isStaticMethod(method.signature)) {
            out.add(TARGET).add(" obj: ").add(summary.simpleName);

            if (!method.parameters.isEmpty())
                out.add(", ");
        }

        var pCounter = method.parameters.size();
        for (VariableSummary p : method.parameters.values()) {
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

    private void renderAutomatonAnnotations() {
        for (var ann : summary.annotations)
            if (ann.startsWith(GENERIC_PREFIX))
                out.addln(ann);

        // inline automaton annotations
        out.add(PUBLIC).add(" ");
    }

    private void renderAutomatonHeader() {
        out.addln();
        out.addln("// automata");
        out.addln();

        renderAutomatonAnnotations();
        out.add("automaton ").add(automatonName).add(": ").addln(summary.simpleName);
        out.addln("(");
        out.addln(")");
    }

    private void renderCompanionTypeAnnotation() {
        out.add("@For(automaton=\"").add(automatonName).add("\", insteadOf=\"").add(summary.typeName).addln("\")");
    }

    private void renderSemanticTypes() {
        out.addln();
        out.addln("// local semantic types");

        out.addln();
        for (var p : summary.allGenericTypeVariables)
            out.addln("@TypeMapping(typeVariable=true) typealias " + p + " = Object;");
        out.addln();

        renderCompanionTypeAnnotation();
        renderClassTypeAnnotations();

        out.add("type ").add(summary.simpleName).addln();
        out.addln("{");
        out.beginBlock();

        renderSpecialConstants();

        out.endBlock();
        out.addln("}");
        out.addln();
    }

    private void renderImports() {
        out.add(IMPORT).addln(" \"java-common.lsl\";");

        for (var ref : summary.imports) {
            final var path = ref.replace('.', '/');
            out.add(IMPORT).add(" \"").add(path).addln(".lsl\";");
        }

        out.addln();
        out.add(IMPORT).addln(" \"list-actions.lsl\";");
        out.addln();
    }

    private void renderPreamble() {
        out.addln("libsl \"1.1.0\";");
        out.addln();
        out.addln("library \"std:???\"");
        out.addln(TAB + "version \"11\"");
        out.addln(TAB + "language \"Java\"");
        out.addln(TAB + "url \"-\";");
        out.addln();
        out.addln("// imports");
        out.addln();

        renderImports();
    }

    public void render() {
        renderPreamble();

        renderSemanticTypes();

        renderAutomatonHeader();
        out.addln("{");
        out.beginBlock();

        out.addln("// states and shifts");
        out.addln();

        out.addln("initstate Initialized;");
        out.addln();

        renderMethodBatch("constructors", summary.constructors.values());
        renderMethodBatch("utilities", List.of());
        renderMethodBatch("static methods", summary.staticMethods.values());
        renderMethodBatch("methods", summary.instanceMethods.values());

        out.endBlock();
        out.addln("}");
    }
}
