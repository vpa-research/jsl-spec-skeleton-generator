libsl "1.1.0";

library "std:???"
    version "11"
    language "Java"
    url "-";

// imports

import "java-common.lsl";
import "java/io/_interfaces.lsl";
import "java/lang/_interfaces.lsl";
import "java/util/_interfaces.lsl";

import "list-actions.lsl";


// local semantic types

@TypeMapping(typeVariable=true) typealias E = Object;


// automata

@Parameterized(["E"])
@extends("java.util.AbstractSet<E>")
@implements("java.util.Set<E>")
@implements("java.lang.Cloneable")
@implements("java.io.Serializable")
@public automaton HashSet: int
(
    @private @static @final var serialVersionUID: long = -5024744406713321676;
)
{
    // states and shifts

    initstate Initialized;


    // constructors

    constructor HashSet ()
    {
        action TODO();
    }


    constructor HashSet (@Parameterized(["? extends E"]) c: Collection)
    {
        action TODO();
    }


    constructor HashSet (initialCapacity: int)
    {
        action TODO();
    }


    constructor HashSet (initialCapacity: int, loadFactor: float)
    {
        action TODO();
    }


    @private constructor HashSet (initialCapacity: int, loadFactor: float, dummy: boolean)
    {
        action TODO();
    }


    // utilities

    // static methods

    // methods

    fun add (e: E): boolean
    {
        action TODO();
    }


    fun clear (): void
    {
        action TODO();
    }


    fun clone (): Object
    {
        action TODO();
    }


    fun contains (o: Object): boolean
    {
        action TODO();
    }


    fun isEmpty (): boolean
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun iterator (): Iterator
    {
        action TODO();
    }


    fun remove (o: Object): boolean
    {
        action TODO();
    }


    fun size (): int
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun spliterator (): Spliterator
    {
        action TODO();
    }

}
