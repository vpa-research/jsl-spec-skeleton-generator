libsl "1.1.0";

library "std:???"
    version "11"
    language "Java"
    url "-";

// imports

import "java-common.lsl";
import "java/util/_interfaces.lsl";

import "list-actions.lsl";


// local semantic types

@TypeMapping(typeVariable=true) typealias E = Object;

@For(automaton="HashSetAutomaton", insteadOf="java.util.HashSet")
@Parameterized(["E"])
@extends("java.util.AbstractSet<E>")
@implements("java.util.Set<E>")
@implements("java.lang.Cloneable")
@implements("java.io.Serializable")
@public type HashSet
{
    @static @final var serialVersionUID: long = -5024744406713321676;
}


// automata

@Parameterized(["E"])
@public automaton HashSetAutomaton: HashSet
(
)
{
    // states and shifts

    initstate Initialized;


    // constructors

    constructor `HashSet#HashSet` (@target obj: HashSet)
    {
        action TODO();
    }


    constructor `HashSet#HashSet` (@target obj: HashSet, @Parameterized(["? extends E"]) arg0: Collection)
    {
        action TODO();
    }


    constructor `HashSet#HashSet` (@target obj: HashSet, arg0: int)
    {
        action TODO();
    }


    constructor `HashSet#HashSet` (@target obj: HashSet, arg0: int, arg1: float)
    {
        action TODO();
    }


    constructor `HashSet#HashSet` (@target obj: HashSet, arg0: int, arg1: float, arg2: boolean)
    {
        action TODO();
    }


    // utilities

    // static methods

    // methods

    fun `HashSet#add` (@target obj: HashSet, arg0: E): boolean
    {
        action TODO();
    }


    fun `HashSet#clear` (@target obj: HashSet): void
    {
        action TODO();
    }


    fun `HashSet#clone` (@target obj: HashSet): Object
    {
        action TODO();
    }


    fun `HashSet#contains` (@target obj: HashSet, arg0: Object): boolean
    {
        action TODO();
    }


    fun `HashSet#isEmpty` (@target obj: HashSet): boolean
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun `HashSet#iterator` (@target obj: HashSet): Iterator
    {
        action TODO();
    }


    fun `HashSet#remove` (@target obj: HashSet, arg0: Object): boolean
    {
        action TODO();
    }


    fun `HashSet#size` (@target obj: HashSet): int
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun `HashSet#spliterator` (@target obj: HashSet): Spliterator
    {
        action TODO();
    }

}
