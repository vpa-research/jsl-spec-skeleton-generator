package org.libsl.skeletons;

import org.libsl.skeletons.rendering.AbstractInfoRenderer;
import org.libsl.skeletons.rendering.InfoRendererDirect;
import org.libsl.skeletons.rendering.InfoRendererIndirect;
import org.libsl.skeletons.rendering.InfoRendererPrimary;
import org.libsl.skeletons.sources.runtime.JslClassCache;
import org.libsl.skeletons.summary.ClassSummary;
import org.libsl.skeletons.summary.runtime.ReflectionClassAnalyzer;
import org.libsl.skeletons.util.PrettyPrinter;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * Examples of execution parameters (both (1) and (2) variants should produce the same result):
 * class=java.util.Optional
 * class=java.util.Optional renderer=primary
 * class=java.util.Optional renderer=direct
 * class=java.util.Optional renderer=indirect
 */
public final class JslSpecSkeletonMain {
    private final Properties props = new Properties();

    public JslSpecSkeletonMain(final String[] cliArgs) {
        for (var arg : cliArgs) {
            final var pair = arg.split("=", 2);
            if (pair.length != 2)
                throw new AssertionError("Expecting pairs of values but got '" + arg + "'");

            props.setProperty(pair[0], pair[1]);
        }
    }

    public void run() {
        final var targetClass = getTargetClass();
        final var summary = getClassSummary(targetClass);
        final var renderer = getRenderer(summary);

        renderer.render();
    }

    private String getTargetClass() {
        final var target = props.getProperty("class");
        if (target == null) {
            System.err.println(
                    "[!] Class have not been specified. " +
                    "Use 'class=<canonical-name>' to specify the class for analysis " +
                    "(Note: use '$' separator for sub-classes)");
            System.exit(-1);
        }

        return target;
    }

    private ClassSummary getClassSummary(final String targetClass) {
        final var collectGenerics = !"false".equalsIgnoreCase(props.getProperty("generics", "false"));

        final Supplier<ClassSummary> supplier;

        final var source = props.getProperty("data-source", "reflection");
        switch (source) {
            case "reflection":
                supplier = new ReflectionClassAnalyzer(loadClassFromRuntime(targetClass))::collectInfo;
                break;

            case "bytecode":
                supplier = null;
                break;

            default:
                throw new AssertionError("Unknown class data source: " + source);
        }

        return supplier.get();
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

    private static ClassReader loadClassFromResources(final String canonicalName) {
        final var path = canonicalName.replace('.', '/').concat(".class");
        final var loader = Object.class.getClassLoader();

        try (final var res = loader.getResourceAsStream(path)) {
            assert res != null;

            return new ClassReader(res.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private AbstractInfoRenderer getRenderer(final ClassSummary summary) {
        final var renderer = props.getProperty("renderer", "primary");
        final var printer = new PrettyPrinter();

        switch (renderer.toLowerCase()) {
            case "primary":
                return new InfoRendererPrimary(summary, printer);

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
        new JslSpecSkeletonMain(args).run();
    }
}
