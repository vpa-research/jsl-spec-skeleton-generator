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


    @private constructor Optional (arg0: T)
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
    @static fun of (arg0: T): Optional
    {
        action TODO();
    }


    @Parameterized(["T"])
    @ParameterizedResult(["T"])
    @static fun ofNullable (arg0: T): Optional
    {
        action TODO();
    }


    // methods

    fun equals (arg0: Object): boolean
    {
        action TODO();
    }


    @ParameterizedResult(["T"])
    fun filter (@Parameterized(["? super T"]) arg0: Predicate): Optional
    {
        action TODO();
    }


    @Parameterized(["U"])
    @ParameterizedResult(["U"])
    fun flatMap (@Parameterized(["? super T", "? extends Optional<? extends U>"]) arg0: Function): Optional
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


    fun ifPresent (@Parameterized(["? super T"]) arg0: Consumer): void
    {
        action TODO();
    }


    fun ifPresentOrElse (@Parameterized(["? super T"]) arg0: Consumer, arg1: Runnable): void
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
    fun map (@Parameterized(["? super T", "? extends U"]) arg0: Function): Optional
    {
        action TODO();
    }


    @ParameterizedResult(["T"])
    fun or (@Parameterized(["? extends Optional<? extends T>"]) arg0: Supplier): Optional
    {
        action TODO();
    }


    fun orElse (arg0: T): T
    {
        action TODO();
    }


    fun orElseGet (@Parameterized(["? extends T"]) arg0: Supplier): T
    {
        action TODO();
    }


    fun orElseThrow (): T
    {
        action TODO();
    }


    @Parameterized(["X extends java.lang.Throwable"])
    @throws(["X"])
    fun orElseThrow (@Parameterized(["? extends X"]) arg0: Supplier): T
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
