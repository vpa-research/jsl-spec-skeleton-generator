package org.libsl.skeletons;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.libsl.skeletons.rendering.InfoRendererPrimary;
import org.libsl.skeletons.sources.runtime.JslClassCache;
import org.libsl.skeletons.summary.runtime.ReflectionClassAnalyzer;
import org.libsl.skeletons.util.PrettyPrinter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimaryRendererTest {
    private static final String[] TEST_FILES = new String[] {
            "java.util.IntSummaryStatistics",
    };

    private static Stream<Arguments> testExamples(){
        return Arrays.stream(TEST_FILES).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("testExamples")
    void render(final String target) throws IOException {
        final String targetSrc = TestUtils.loadExampleFile("skeletons/primary/" + target + ".lsl");
        final Class<?> targetClass = JslClassCache.findClass(target);

        final var summary = new ReflectionClassAnalyzer(targetClass, false).collectInfo();

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8)) {
            new InfoRendererPrimary(summary, new PrettyPrinter(ps)).render();
        }
        final String resultSrc = baos.toString(StandardCharsets.UTF_8);

        assertEquals(targetSrc, resultSrc);
    }
}