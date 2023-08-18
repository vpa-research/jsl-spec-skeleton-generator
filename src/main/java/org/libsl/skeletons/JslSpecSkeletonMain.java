package org.libsl.skeletons;

import org.libsl.skeletons.util.JslClassCache;
import org.libsl.skeletons.util.PrettyPrinter;

import java.util.Properties;

/**
 * Examples of execution parameters (both (1) and (2) variants should produce the same result):
 * class=java.util.Optional
 * class=java.util.Optional renderer=primary
 * class=java.util.Optional renderer=direct
 * class=java.util.Optional renderer=indirect
 */
final class JslSpecSkeletonMain {
    private final Properties props = new Properties();

    private JslSpecSkeletonMain(final String[] cliArgs) {
        for (var arg : cliArgs) {
            final var pair = arg.split("=", 2);
            if (pair.length != 2)
                throw new AssertionError("Expecting pairs of values but got '" + arg + "'");

            props.setProperty(pair[0], pair[1]);
        }
    }

    private void run() {
        final var targetClass = getTargetClass();
        final var summary = getClassSummary(targetClass);
        final var renderer = getRenderer(summary);

        renderer.render();
    }

    private Class<?> getTargetClass() {
        final var target = props.getProperty("class", "<unspecified>");

        final var targetClass = JslClassCache.findClass(target);
        if (targetClass == null) {
            System.err.println("[!] Unable to find class: " + target);
            System.exit(-1);
        }

        return targetClass;
    }

    private ClassSummary getClassSummary(final Class<?> targetClass) {
        final var collectGenerics = !"false".equalsIgnoreCase(props.getProperty("generics", "false"));

        return new JslClassSummaryConstructor(targetClass)
                .collectInfo();
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

    public static void main(final String[] args) {
        new JslSpecSkeletonMain(args).run();
    }
}
