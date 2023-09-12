package org.libsl.skeletons.rendering;

import org.libsl.skeletons.summary.ClassSummary;
import org.libsl.skeletons.summary.KeywordFilter;
import org.libsl.skeletons.summary.MethodSummary;
import org.libsl.skeletons.summary.VariableSummary;
import org.libsl.skeletons.util.PrettyPrinter;

import java.util.*;
import java.util.function.Predicate;

import static org.libsl.skeletons.summary.Annotations.*;

public final class InfoRendererTests extends AbstractInfoRenderer {
    private static final String METHOD_NAME_PREFIX = "test_";
    private static final String DEFAULT_BODY = "return -1;";

    private final ClassSummary summary;
    private final String outputClassName;
    private final String automatonName;
    private final Map<String, Integer> nextUUID = new IdentityHashMap<>();

    public InfoRendererTests(final ClassSummary summary, final PrettyPrinter out) {
        super(out);
        this.summary = summary;
        this.automatonName = summary.simpleName + "Automaton";
        this.outputClassName = summary.simpleName + "_Tests";
    }

    private void renderMethod(final MethodSummary method) {
        out.addln();
        out.addln();

        // potential origin reference
        out.addln("// %s::%s", automatonName, method.signature);

        // modifiers
        out.add("public static ");

        // return value type
        out.add("%s ", "int");

        // keyword + name
        final var fullMethodName = METHOD_NAME_PREFIX + method.simpleName + "_" + getUniqueSuffix(method);
        out.add(fullMethodName);

        // parameters
        out.add("(%s) ", "final int execution");

        // method body
        out.addln("{");
        out.beginBlock();

        out.addln(DEFAULT_BODY);

        out.endBlock();
        out.addln("}");
    }

    private String getUniqueSuffix(final MethodSummary method) {
        final var uuid = nextUUID.compute(method.simpleName, (m, id) -> id != null ? id + 1 : 0);
        return uuid.toString();
    }

    private void renderMethodBatch(final String batchTitle, final Collection<MethodSummary> batch) {
        out.addln();
        out.add("// ").add(batchTitle);

        batch.forEach(this::renderMethod);

        out.addln();
    }

    private void renderClassHeader() {
        out.add("public final class %s ", outputClassName);
    }

    private void renderPreamble() {
        out.addln("package ???.%s;", summary.packageName);
        out.addln();
    }

    public void render() {
        renderPreamble();

        renderClassHeader();
        out.addln("{");
        out.beginBlock();

        renderMethodBatch("internal variables", List.of());
        renderMethodBatch("constructors", summary.constructors.values());
        renderMethodBatch("static methods", summary.staticMethods.values());
        renderMethodBatch("methods", summary.instanceMethods.values());

        out.endBlock();
        out.addln("}");
    }
}
