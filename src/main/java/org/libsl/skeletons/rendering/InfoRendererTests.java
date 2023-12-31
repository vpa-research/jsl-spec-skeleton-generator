package org.libsl.skeletons.rendering;

import org.libsl.skeletons.summary.Annotations;
import org.libsl.skeletons.summary.ClassSummary;
import org.libsl.skeletons.summary.MethodSummary;
import org.libsl.skeletons.util.PrettyPrinter;

import java.util.*;

public final class InfoRendererTests extends AbstractInfoRenderer {
    private static final String TARGET_PACKAGE_PREFIX = "approximations";
    private static final String ANNOTATION_PACKAGE = TARGET_PACKAGE_PREFIX;
    private static final String ANNOTATION_NAME = "Test";
    private static final String CLASS_PREFIX = "_Tests";
    private static final String METHOD_NAME_PREFIX = "test_";
    private static final String METHOD_RETURN_TYPE = "int";
    private static final String METHOD_BODY = "return -1;";
    private static final String WARNING_SUPPRESSION = "SuppressWarnings({\"unused\"})";

    private final ClassSummary summary;
    private final String outputClassName;
    private final Map<String, Integer> nextUUID = new IdentityHashMap<>();

    public InfoRendererTests(final ClassSummary summary, final PrettyPrinter out) {
        super(out);
        this.summary = summary;
        this.outputClassName = summary.simpleName + CLASS_PREFIX;
    }

    private void renderMethod(final MethodSummary method) {
        // skipping inaccessible methods
        if (method.annotations.contains(Annotations.PRIVATE) ||
                method.annotations.contains(Annotations.PROTECTED))
            return;

        out.addln();
        out.addln();

        // origin reference as JavaDoc
        out.addln(
                "/**\n * {@link %s.%s#%s}\n */",
                summary.packageName,
                summary.simpleName,
                getMethodJavaDocSignature(method)
        );

        // annotation for signaling/configuration
        out.add("@").addln(ANNOTATION_NAME);

        // modifiers
        out.add("public static ");

        // return value type
        out.add(METHOD_RETURN_TYPE).add(" ");

        // keyword + name
        out.add(METHOD_NAME_PREFIX + fixSpecialName(method.simpleName) + "_" + getUniqueSuffix(method));

        // parameters
        out.add("(%s) ", "final int execution");

        // method body
        out.addln("{");
        out.beginBlock();

        out.addln(METHOD_BODY);

        out.endBlock();
        out.addln("}");
    }

    private String getMethodJavaDocSignature(final MethodSummary method) {
        final var sj = new StringJoiner(", ", "(", ")");

        for (var par : method.parameters.values())
            sj.add(getTypeJavaDocSignature(par.simpleType));

        return fixSpecialName(method.simpleName) + sj;
    }

    private String fixSpecialName(final String name) {
        if (ClassSummary.CONSTRUCTOR_NAME.equals(name))
            return summary.simpleName;

        return name;
    }

    private static String getTypeJavaDocSignature(final String type) {
        if (type.startsWith("array<")) {
            int startPos = "array<".length();
            int endPos = type.length() - 1;
            return getTypeJavaDocSignature(type.substring(startPos, endPos)) + "[]";
        }

        return type;
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
        out.add("@").addln(ANNOTATION_NAME);
        out.add("@").addln(WARNING_SUPPRESSION);
        out.add("public final class %s ", outputClassName);
    }

    private void renderPreamble() {
        out.addln("package %s.%s;", TARGET_PACKAGE_PREFIX, summary.packageName);
        out.addln();

        out.addln("import %s.%s;", ANNOTATION_PACKAGE, ANNOTATION_NAME);
        out.addln();

        if (!summary.imports.isEmpty()) {
            for (var type : summary.imports)
                out.addln("import %s;", type);
            out.addln();
        }

        out.addln("import %s.%s;", summary.packageName, summary.simpleName);
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
