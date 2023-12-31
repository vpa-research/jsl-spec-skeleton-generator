libsl "1.1.0";

library "std:???"
    version "11"
    language "Java"
    url "-";

// imports

import "java-common.lsl";
import "java/io/Serializable.lsl";
import "java/lang/Cloneable.lsl";
import "java/lang/Object.lsl";
import "java/util/AbstractSet.lsl";
import "java/util/Collection.lsl";
import "java/util/Iterator.lsl";
import "java/util/Set.lsl";
import "java/util/Spliterator.lsl";

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

    constructor `<init>` ()
    {
        action TODO();
    }


    constructor `<init>` (@Parameterized(["? extends E"]) c: Collection)
    {
        action TODO();
    }


    constructor `<init>` (initialCapacity: int)
    {
        action TODO();
    }


    constructor `<init>` (initialCapacity: int, loadFactor: float)
    {
        action TODO();
    }


    @private constructor `<init>` (initialCapacity: int, loadFactor: float, dummy: boolean)
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
