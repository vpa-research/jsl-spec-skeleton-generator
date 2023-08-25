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
    @private @static @final var serialVersionUID: long = -5024744406713321676;
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


    constructor `HashSet#HashSet` (@target obj: HashSet, @Parameterized(["? extends E"]) c: Collection)
    {
        action TODO();
    }


    constructor `HashSet#HashSet` (@target obj: HashSet, initialCapacity: int)
    {
        action TODO();
    }


    constructor `HashSet#HashSet` (@target obj: HashSet, initialCapacity: int, loadFactor: float)
    {
        action TODO();
    }


    @private constructor `HashSet#HashSet` (@target obj: HashSet, initialCapacity: int, loadFactor: float, dummy: boolean)
    {
        action TODO();
    }


    // utilities

    // static methods

    // methods

    fun `HashSet#add` (@target obj: HashSet, e: E): boolean
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


    fun `HashSet#contains` (@target obj: HashSet, o: Object): boolean
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


    fun `HashSet#remove` (@target obj: HashSet, o: Object): boolean
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
