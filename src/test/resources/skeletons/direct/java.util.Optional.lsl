libsl "1.1.0";

library "std:???"
    version "11"
    language "Java"
    url "-";

// imports

import "java-common.lsl";
import "java/lang/_interfaces.lsl";
import "java/util/function/_interfaces.lsl";
import "java/util/stream/_interfaces.lsl";

import "list-actions.lsl";


// local semantic types

@TypeMapping(typeVariable=true) typealias T = Object;
@TypeMapping(typeVariable=true) typealias U = Object;
@TypeMapping(typeVariable=true) typealias X = Object;


// automata

@Parameterized(["T"])
@public @final automaton Optional: int
(
)
{
    // states and shifts

    initstate Initialized;


    // constructors

    @private constructor Optional ()
    {
        action TODO();
    }


    @private constructor Optional (value: T)
    {
        action TODO();
    }


    // utilities

    // static methods

    @Parameterized(["T"])
    @ParameterizedResult(["T"])
    @static fun empty (): Optional
    {
        action TODO();
    }


    @Parameterized(["T"])
    @ParameterizedResult(["T"])
    @static fun of (value: T): Optional
    {
        action TODO();
    }


    @Parameterized(["T"])
    @ParameterizedResult(["T"])
    @static fun ofNullable (value: T): Optional
    {
        action TODO();
    }


    // methods

    fun equals (obj: Object): boolean
    {
        action TODO();
    }


    @ParameterizedResult(["T"])
    fun filter (@Parameterized(["? super T"]) predicate: Predicate): Optional
    {
        action TODO();
    }


    @Parameterized(["U"])
    @ParameterizedResult(["U"])
    fun flatMap (@Parameterized(["? super T", "? extends Optional<? extends U>"]) mapper: Function): Optional
    {
        action TODO();
    }


    fun get (): T
    {
        action TODO();
    }


    fun hashCode (): int
    {
        action TODO();
    }


    fun ifPresent (@Parameterized(["? super T"]) _action: Consumer): void
    {
        action TODO();
    }


    fun ifPresentOrElse (@Parameterized(["? super T"]) _action: Consumer, emptyAction: Runnable): void
    {
        action TODO();
    }


    fun isEmpty (): boolean
    {
        action TODO();
    }


    fun isPresent (): boolean
    {
        action TODO();
    }


    @Parameterized(["U"])
    @ParameterizedResult(["U"])
    fun map (@Parameterized(["? super T", "? extends U"]) mapper: Function): Optional
    {
        action TODO();
    }


    @ParameterizedResult(["T"])
    fun or (@Parameterized(["? extends Optional<? extends T>"]) supplier: Supplier): Optional
    {
        action TODO();
    }


    fun orElse (other: T): T
    {
        action TODO();
    }


    fun orElseGet (@Parameterized(["? extends T"]) supplier: Supplier): T
    {
        action TODO();
    }


    fun orElseThrow (): T
    {
        action TODO();
    }


    @Parameterized(["X extends java.lang.Throwable"])
    @throws(["X"])
    fun orElseThrow (@Parameterized(["? extends X"]) exceptionSupplier: Supplier): T
    {
        action TODO();
    }


    @ParameterizedResult(["T"])
    fun stream (): Stream
    {
        action TODO();
    }


    fun toString (): String
    {
        action TODO();
    }

}
