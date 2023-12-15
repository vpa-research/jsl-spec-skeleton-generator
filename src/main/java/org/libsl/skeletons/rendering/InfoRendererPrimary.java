package org.libsl.skeletons.rendering;

import org.libsl.skeletons.summary.*;
import org.libsl.skeletons.util.PrettyPrinter;
import org.libsl.skeletons.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Predicate;

import static org.libsl.skeletons.summary.Annotations.*;

public final class InfoRendererPrimary extends AbstractInfoRenderer {
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

        // a comment on the source for the method
        if (method.originClassName != null)
            out.add("// within ").addln(method.originClassName);

        for (var ann : method.annotations)
            if (INLINE_ANNOTATIONS.contains(ann))
                out.add(ann).add(" ");

        // keyword + name
        final var kwd = method.isConstructor ? CONSTRUCTOR : FUNCTION;
        out.add(kwd).add(" ").add(METHOD_NAME_PREFIX).add(StringUtils.addBackticksIfNecessary(
                method.simpleName
        ));

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

        if (summary.isInterface)
            out.add(Annotations.INTERFACE).add(" ");
        else if (summary.isAbstract)
            out.add(Annotations.ABSTRACT).add(" ");

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

            renderShiftCollection("constructors", summary.constructors.values(), false);
            renderShiftCollection("static operations", summary.staticMethods.values(), true);

            out.endBlock();
            out.addln("];");
        }
        out.addln();

        out.addln("shift %s -> %s by [", "Initialized", "self");
        out.beginBlock();

        renderShiftCollection("instance methods", summary.instanceMethods.values(), false);

        out.endBlock();
        out.addln("];");
    }

    private void renderShiftCollection(final String section,
                                       final Collection<MethodSummary> methods,
                                       final boolean isStatic) {
        if (methods.isEmpty())
            return;

        out.add("// ").addln(section);

        for (var m : methods) {
            final String suffix;

            if (summary.hasOverloads(m.simpleName)) {
                // render parameters
                final var sj = new StringJoiner(", ", " (", ")");
                if (!isStatic)
                    sj.add(summary.simpleName);

                for (var parameter : m.parameters.values())
                    sj.add(parameter.simpleType);

                suffix = sj.toString();
            } else {
                suffix = "";
            }

            out.addln("%s%s,", StringUtils.addBackticksIfNecessary(m.simpleName), suffix);
        }
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
