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

@For(automaton="OptionalAutomaton", insteadOf="java.util.Optional")
@Parameterized("T")
@public @final type Optional
{
}


// automata

@Parameterized("T")
@public automaton OptionalAutomaton: Optional
(
)
{
    // states and shifts

    initstate Initialized;


    // constructors

    @private constructor `Optional#Optional` (@target obj: Optional)
    {
        action TODO();
    }


    @private constructor `Optional#Optional` (@target obj: Optional, arg0: T)
    {
        action TODO();
    }


    // utilities

    // static methods

    @Parameterized("T")
    @ParameterizedResult("T")
    @static fun `Optional#empty` (): Optional
    {
        action TODO();
    }


    @Parameterized("T")
    @ParameterizedResult("T")
    @static fun `Optional#of` (arg0: T): Optional
    {
        action TODO();
    }


    @Parameterized("T")
    @ParameterizedResult("T")
    @static fun `Optional#ofNullable` (arg0: T): Optional
    {
        action TODO();
    }


    // methods

    fun `Optional#equals` (@target obj: Optional, arg0: Object): boolean
    {
        action TODO();
    }


    @ParameterizedResult("T")
    fun `Optional#filter` (@target obj: Optional, @Parameterized("? super T") arg0: Predicate): Optional
    {
        action TODO();
    }


    @Parameterized("U")
    @ParameterizedResult("U")
    fun `Optional#flatMap` (@target obj: Optional, @Parameterized("? super T, ? extends Optional<? extends U>") arg0: Function): Optional
    {
        action TODO();
    }


    fun `Optional#get` (@target obj: Optional): T
    {
        action TODO();
    }


    fun `Optional#hashCode` (@target obj: Optional): int
    {
        action TODO();
    }


    fun `Optional#ifPresent` (@target obj: Optional, @Parameterized("? super T") arg0: Consumer): void
    {
        action TODO();
    }


    fun `Optional#ifPresentOrElse` (@target obj: Optional, @Parameterized("? super T") arg0: Consumer, arg1: Runnable): void
    {
        action TODO();
    }


    fun `Optional#isEmpty` (@target obj: Optional): boolean
    {
        action TODO();
    }


    fun `Optional#isPresent` (@target obj: Optional): boolean
    {
        action TODO();
    }


    @Parameterized("U")
    @ParameterizedResult("U")
    fun `Optional#map` (@target obj: Optional, @Parameterized("? super T, ? extends U") arg0: Function): Optional
    {
        action TODO();
    }


    @ParameterizedResult("T")
    fun `Optional#or` (@target obj: Optional, @Parameterized("? extends Optional<? extends T>") arg0: Supplier): Optional
    {
        action TODO();
    }


    fun `Optional#orElse` (@target obj: Optional, arg0: T): T
    {
        action TODO();
    }


    fun `Optional#orElseGet` (@target obj: Optional, @Parameterized("? extends T") arg0: Supplier): T
    {
        action TODO();
    }


    fun `Optional#orElseThrow` (@target obj: Optional): T
    {
        action TODO();
    }


    @Parameterized("X extends java.lang.Throwable")
    @throws(["X"])
    fun `Optional#orElseThrow` (@target obj: Optional, @Parameterized("? extends X") arg0: Supplier): T
    {
        action TODO();
    }


    @ParameterizedResult("T")
    fun `Optional#stream` (@target obj: Optional): Stream
    {
        action TODO();
    }


    fun `Optional#toString` (@target obj: Optional): String
    {
        action TODO();
    }

}