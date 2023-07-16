package org.libsl.skeletons.util;

public abstract class AbstractBuilder<B extends AbstractBuilder<B, R>, R> {
    protected AbstractBuilder() {
    }

    public abstract R build();
}
