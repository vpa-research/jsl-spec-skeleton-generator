package org.libsl.skeletons;

import org.libsl.skeletons.util.PrettyPrinter;

public abstract class AbstractInfoRenderer {
    protected static final String IMPORT = "import";
    protected static final String CONSTRUCTOR = "constructor";
    protected static final String FUNCTION = "fun";
    protected static final String SUBROUTINE = "proc";

    protected final PrettyPrinter out;

    protected AbstractInfoRenderer(final PrettyPrinter out) {
        this.out = out;
    }

    public abstract void render();
}
