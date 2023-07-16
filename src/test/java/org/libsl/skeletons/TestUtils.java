package org.libsl.skeletons;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class TestUtils {
    private TestUtils() {
    }

    public static String loadExampleFile(final String path) throws IOException {
        try(final InputStream s = TestUtils.class.getClassLoader().getResourceAsStream(path)) {
            assert s != null;

            return new String(s.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
