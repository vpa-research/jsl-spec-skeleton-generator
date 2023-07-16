package org.libsl.skeletons.util;

import java.nio.charset.StandardCharsets;
import java.util.zip.*;

public final class HashingUtils {
    private HashingUtils() {
    }

    private static int fromBlankChecksum(final Checksum c, final String data) {
        c.update(data.getBytes(StandardCharsets.UTF_8));
        return (int) c.getValue();
    }

    public static int adler32(final String data) {
        return fromBlankChecksum(new Adler32(), data);
    }

    public static int crc32(final String data) {
        return fromBlankChecksum(new CRC32(), data);
    }

    public static int crc32c(final String data) {
        return fromBlankChecksum(new CRC32C(), data);
    }
}
