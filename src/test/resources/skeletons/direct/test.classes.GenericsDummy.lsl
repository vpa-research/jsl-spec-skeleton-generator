libsl "1.1.0";

library "std:???"
    version "11"
    language "Java"
    url "-";

// imports

import "java-common.lsl";
import "java/lang/_interfaces.lsl";
import "java/util/function/_interfaces.lsl";

import "list-actions.lsl";


// local semantic types

@TypeMapping(typeVariable=true) typealias A = Object;
@TypeMapping(typeVariable=true) typealias B = Object;
@TypeMapping(typeVariable=true) typealias C = Object;


// automata

@public @final automaton GenericsDummy: int
(
)
{
    // states and shifts

    initstate Initialized;


    // constructors

    @private constructor GenericsDummy ()
    {
        action TODO();
    }


    // utilities

    // static methods

    // methods

    @Parameterized(["A"])
    fun aaaaa (x: A): void
    {
        action TODO();
    }


    @Parameterized(["A extends java.lang.Runnable"])
    fun bbbbb (x: A): void
    {
        action TODO();
    }


    @Parameterized(["A extends java.lang.Class<A>"])
    fun ccccc (x: A): void
    {
        action TODO();
    }


    @Parameterized(["A extends java.lang.Class<A> & java.lang.Runnable & java.lang.Iterable<A>"])
    fun ddddd (x: A): void
    {
        action TODO();
    }


    @Parameterized(["A extends java.lang.Iterable<A> & java.lang.Runnable", "B"])
    fun eeeee (@Parameterized(["? super java.lang.Iterable<B>"]) x: Iterable): void
    {
        action TODO();
    }


    @Parameterized(["A extends java.lang.Iterable<B>", "B extends java.lang.Iterable<C>", "C extends java.lang.Iterable<A>"])
    fun fffff (x: A): void
    {
        action TODO();
    }


    @Parameterized(["A extends java.lang.Iterable<B>", "B extends java.lang.Iterable<A> & java.lang.Comparable<C>", "C extends java.lang.Iterable<B>"])
    fun fffff__2 (x: C): void
    {
        action TODO();
    }


    @Parameterized(["A", "B extends java.lang.Iterable<? super A>"])
    fun ggggg (x: B): void
    {
        action TODO();
    }


    fun hhhhh (@Parameterized(["?"]) x: Iterable): void
    {
        action TODO();
    }


    fun iiiii (@Parameterized(["? extends java.lang.Runnable"]) x: Iterable): void
    {
        action TODO();
    }


    fun jjjjj (@Parameterized(["? super java.lang.Runnable"]) x: Iterable): void
    {
        action TODO();
    }


    fun kkkkk (@Parameterized(["? super java.lang.Iterable<?>"]) x: Iterable): void
    {
        action TODO();
    }


    @Parameterized(["A", "B extends java.util.function.BiConsumer<A, C>", "C"])
    fun lllll (x: B): void
    {
        action TODO();
    }

}
