package org.libsl.skeletons.util;

import java.io.File;
import java.nio.file.Path;

public final class PathUtils {
    private PathUtils() {
    }

    public static Path pathFromPackage(final String packageName) {
        return new File(packageName.replace('.', '/')).toPath();
    }
}
