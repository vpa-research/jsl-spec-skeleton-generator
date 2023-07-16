package org.libsl.skeletons;

import org.libsl.skeletons.util.JslClassCache;
import org.libsl.skeletons.util.PrettyPrinter;

/**
 * Examples of execution parameters (both (2) and (3) variants should produce the same result):
 * java.util.Optional indirect
 * java.util.Optional direct
 * java.util.Optional
 */
final class JslSpecSkeletonMain {
    public static void main(String[] args) {
        final var target = args[0];
        final var targetClass = JslClassCache.findClass(target);

        final var summary = new JslClassSummaryConstructor(targetClass).collectInfo();

        final AbstractInfoRenderer r;
        if (args.length >= 2 && "indirect".equals(args[1]))
            r = new InfoRendererIndirect(summary, new PrettyPrinter());
        else
            r = new InfoRendererDirect(summary, new PrettyPrinter());

        r.render();
    }
}
