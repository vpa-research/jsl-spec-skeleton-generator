package org.libsl.skeletons.rendering;

import org.libsl.skeletons.summary.ClassSummary;
import org.libsl.skeletons.summary.KeywordFilter;
import org.libsl.skeletons.summary.MethodSummary;
import org.libsl.skeletons.summary.VariableSummary;
import org.libsl.skeletons.util.PrettyPrinter;
import org.libsl.skeletons.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static org.libsl.skeletons.summary.Annotations.*;
import static org.libsl.skeletons.util.PrettyPrinter.TAB;

public final class InfoRendererDirect extends AbstractInfoRenderer {
    private final ClassSummary summary;

    public InfoRendererDirect(final ClassSummary summary, final PrettyPrinter out) {
        super(out);
        this.summary = summary;
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
        out.add(method.isConstructor ? CONSTRUCTOR : FUNCTION).add(" ").add(
                StringUtils.addBackticksIfNecessary(method.simpleName)
        );

        // parameters
        out.add(" (");
        int pCounter = method.parameters.size();
        for (var p : method.parameters.values()) {
            p.annotations.forEach(a -> out.add(a).add(" "));

            out.add(KeywordFilter.renameIfKeyword(p.name)).add(": ").add(p.simpleType);

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
        int rendered = 0;
        for (var sc : summary.specialConstants.values()) {
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

    private void renderAutomatonAnnotations() {
        for (var ann : summary.annotations)
            if (!INLINE_ANNOTATIONS.contains(ann))
                out.addln(ann);

        // inline automaton annotations
        out.add(PUBLIC).add(" ");
        for (var ann : summary.annotations)
            if (INLINE_ANNOTATIONS.contains(ann))
                out.add(ann).add(" ");
    }

    private void renderAutomatonHeader() {
        out.addln();
        out.addln("// automata");
        out.addln();

        renderAutomatonAnnotations();

        out.add("automaton ").add(summary.simpleName).addln(": int");
        out.addln("(");
        out.beginBlock();

        renderSpecialConstants();

        out.endBlock();
        out.addln(")");
    }

    private void renderSemanticTypes() {
        out.addln();
        out.addln("// local semantic types");

        out.addln();
        for (var p : summary.allGenericTypeVariables)
            out.addln("@TypeMapping(typeVariable=true) typealias %s = Object;", p);
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
