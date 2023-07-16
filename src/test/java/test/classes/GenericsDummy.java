package test.classes;

import java.util.function.BiConsumer;

/**
 * <b>TEST CLASS. DO NOT REMOVE!</b>
 */
public final class GenericsDummy {
    private GenericsDummy() {
    }

    public <A> void aaaaa(A x) {
    }

    public <A extends Runnable> void bbbbb(A x) {
    }

    public <A extends Class<A>> void ccccc(A x) {
    }

    public <A extends Class<A> & Runnable & Iterable<A>> void ddddd(A x) {
    }

    public <A extends Iterable<A> & Runnable, B> void eeeee(Iterable<? super Iterable<B>> x) {
    }

    public <A extends Iterable<B>, B extends Iterable<C>, C extends Iterable<A>> void fffff(A x) {
    }

    public <A extends Iterable<B>,
            B extends Iterable<A> & Comparable<C>,
            C extends Iterable<B>>
    void fffff__2(C x) {
    }

    public <A, B extends Iterable<? super A>> void ggggg(B x) {
    }

    public void hhhhh(Iterable<?> x) {
    }

    public void iiiii(Iterable<? extends Runnable> x) {
    }

    public void jjjjj(Iterable<? super Runnable> x) {
    }

    public void kkkkk(Iterable<? super Iterable<?>> x) {
    }

    public <A, B extends BiConsumer<A, C>, C> void lllll(B x) {
    }
}
