package org.libsl.skeletons.util;

import java.io.PrintStream;

public class PrettyPrinter {
    public static final String TAB = "    ";
    protected static final String[] TABS = new String[96];
    protected static final String LINE_SEPARATOR = "\\R";

    protected final PrintStream stream;
    protected int indent = 0;
    protected boolean startedPrinting = false;

    static {
        TABS[0] = "";
        for (int i = 1; i < TABS.length; i++)
            TABS[i] = TABS[i - 1] + TAB;
    }

    public PrettyPrinter() {
        this(System.out);
    }

    public PrettyPrinter(final PrintStream stream) {
        this.stream = stream;
    }

    protected final void renderIndentation() {
        stream.print(TABS[indent]);
    }

    protected final void renderIndentationIfNotStarted() {
        if (!startedPrinting)
            renderIndentation();
        startedPrinting = true;
    }

    protected final void renderStrings(final String[] lines, final int limit) {
        for (int i = 0, j = limit - 1; i < limit; i++) {
            renderString(lines[i]);
            renderNewLine();

            if (i != j)
                renderIndentation();
        }
    }

    protected final void increaseIndentation() {
        indent += 1;

        if (indent >= TABS.length)
            throw new RuntimeException("Run out of indentation");
    }

    protected final void decreaseIndentation() {
        indent -= 1;

        if (indent < 0)
            throw new RuntimeException("Negative indentation");
    }

    protected void renderString(final String s) {
        stream.print(s);
    }

    protected void renderNewLine() {
        stream.println();
    }

    public void beginBlock() {
        increaseIndentation();
    }

    public void endBlock() {
        decreaseIndentation();
    }

    public PrettyPrinter add(final String s) {
        renderIndentationIfNotStarted();

        final var lines = s.split(LINE_SEPARATOR);
        final var lastIdx = lines.length - 1;
        renderStrings(lines, lastIdx);

        stream.print(lines[lastIdx]);

        startedPrinting = !lines[lastIdx].isEmpty();

        return this;
    }

    public PrettyPrinter add(final String fmt, final Object... values) {
        return add(String.format(fmt, values));
    }

    public void addln() {
        stream.println();

        startedPrinting = false;
    }

    public void addln(final String s) {
        renderIndentationIfNotStarted();

        final var lines = s.split(LINE_SEPARATOR);
        renderStrings(lines, lines.length);

        startedPrinting = false;
    }

    public void addln(final String fmt, final Object... values) {
        addln(String.format(fmt, values));
    }
}
