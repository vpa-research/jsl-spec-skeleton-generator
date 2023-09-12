package org.libsl.skeletons;

import org.libsl.skeletons.rendering.*;
import org.libsl.skeletons.sources.runtime.JslClassCache;
import org.libsl.skeletons.summary.ClassSummary;
import org.libsl.skeletons.summary.ClassSummaryProducer;
import org.libsl.skeletons.summary.runtime.ReflectionClassAnalyzer;
import org.libsl.skeletons.summary.runtime.ReflectionClassAnalyzerGeneric;
import org.libsl.skeletons.util.PrettyPrinter;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Function;

/**
 * Examples of execution parameters (both (1) and (2) variants should produce the same result):
 * class=java.util.Optional
 * class=java.util.Optional renderer=primary
 * class=java.util.Optional renderer=direct
 * class=java.util.Optional renderer=indirect
 * <p>
 * Complete usage example:
 * class=java.util.ArrayList$ListItr renderer=primary generics=false include-inherited=true data-source=reflection
 */
public final class JslSpecSkeletonMain {
    private static final Map<String, String> DEFAULTS;
    public static final String PROP_CLASS_TARGET = "class";
    public static final String PROP_GENERICS = "generics";
    public static final String PROP_INCLUDE_INHERITED_METHODS = "include-inherited";
    public static final String PROP_DATA_SOURCE = "data-source";
    public static final String PROP_RENDERER = "renderer";

    private final Map<String, String> props = new HashMap<>(DEFAULTS);

    static {
        DEFAULTS = Map.of(
                PROP_GENERICS, "false",
                PROP_INCLUDE_INHERITED_METHODS, "false",
                PROP_DATA_SOURCE, "reflection",
                PROP_RENDERER, "primary"
        );
    }

    public JslSpecSkeletonMain(final String[] cliArgs) {
        for (var arg : cliArgs) {
            final var pair = arg.split("=", 2);
            if (pair.length != 2)
                throw new AssertionError("Expecting pairs of values but got '" + arg + "'");

            props.put(pair[0], pair[1]);
        }
    }

    public void printConfiguration(final PrintStream out) {
        for (var k : new TreeSet<>(props.keySet())) {
            out.print("- ");
            out.print(k);
            out.print(" = ");
            out.println(props.get(k));
        }
    }

    public void run() {
        final var targetClass = getTargetClass();
        final var analyzer = getClassAnalyzer();

        final var summary = analyzer.apply(targetClass);
        final var renderer = getRenderer(summary);

        renderer.render();
    }

    private String getTargetClass() {
        final var target = props.get(PROP_CLASS_TARGET);
        if (target == null) {
            System.err.println(
                    "[!] Class have not been specified. " +
                            "Use 'class=<canonical-name>' to specify the class for analysis " +
                            "(Note: use '$' separator for sub-classes)");
            System.exit(-1);
        }

        return target;
    }

    private Function<String, ClassSummary> getClassAnalyzer() {
        final var collectGenerics =
                !"false".equalsIgnoreCase(props.get(PROP_GENERICS));

        final var includeInheritedMethods =
                !"false".equalsIgnoreCase(props.get(PROP_INCLUDE_INHERITED_METHODS));

        final var dataSource = props.get(PROP_DATA_SOURCE);
        switch (dataSource) {
            case "reflection":
                return canonicalName -> {
                    final var source = loadClassFromRuntime(canonicalName);

                    final ClassSummaryProducer analyzer;
                    if (collectGenerics)
                        analyzer = new ReflectionClassAnalyzerGeneric(
                                source,
                                includeInheritedMethods
                        );
                    else
                        analyzer = new ReflectionClassAnalyzer(
                                source,
                                includeInheritedMethods
                        );

                    return analyzer.collectInfo();
                };

            case "bytecode":
                throw new Error("TODO");

            default:
                throw new AssertionError("Unknown class data source: " + dataSource);
        }
    }

    private static Class<?> loadClassFromRuntime(final String canonicalName) {
        // check the version of runtime environment
        validateJREVersion();

        // continue as usual
        final var target = JslClassCache.findClass(canonicalName);
        if (target == null) {
            System.err.println("[!] Class not found: " + canonicalName);
            System.exit(-1);
        }
        return target;
    }

    private AbstractInfoRenderer getRenderer(final ClassSummary summary) {
        final var renderer = props.get(PROP_RENDERER);
        final var printer = new PrettyPrinter();

        switch (renderer.toLowerCase()) {
            case "primary":
                return new InfoRendererPrimary(summary, printer);

            case "tests":
                return new InfoRendererTests(summary, printer);

            case "indirect":
                return new InfoRendererIndirect(summary, printer);

            case "direct":
                return new InfoRendererDirect(summary, printer);

            default:
                throw new AssertionError("Unknown renderer kind: " + renderer);
        }
    }

    private static void validateJREVersion() {
        final var jreVersion = Runtime.version().version().get(0);
        if (jreVersion != 11) {
            System.err.println("[!] Unexpected JRE version: " + jreVersion);
            System.exit(-1);
        }
    }

    public static void main(final String[] args) {
        System.err.println("[i] CLI args:");
        for (var arg : args)
            System.err.println("- " + arg);
        System.err.flush();
        System.out.flush();

        final var m = new JslSpecSkeletonMain(args);

        System.err.println("[i] Configuration:");
        m.printConfiguration(System.err);
        System.err.flush();
        System.out.flush();

        m.run();
    }
}
