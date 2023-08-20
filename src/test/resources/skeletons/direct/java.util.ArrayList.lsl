libsl "1.1.0";

library "std:???"
    version "11"
    language "Java"
    url "-";

// imports

import "java-common.lsl";
import "java/lang/_interfaces.lsl";
import "java/util/_interfaces.lsl";
import "java/util/function/_interfaces.lsl";

import "list-actions.lsl";


// local semantic types

@TypeMapping(typeVariable=true) typealias E = Object;


// automata

@Parameterized(["E"])
@extends("java.util.AbstractList<E>")
@implements("java.util.List<E>")
@implements("java.util.RandomAccess")
@implements("java.lang.Cloneable")
@implements("java.io.Serializable")
@public automaton ArrayList: int
(
    @private @static @final var serialVersionUID: long = 8683452581122892189;
)
{
    // states and shifts

    initstate Initialized;


    // constructors

    constructor ArrayList ()
    {
        action TODO();
    }


    constructor ArrayList (@Parameterized(["? extends E"]) arg0: Collection)
    {
        action TODO();
    }


    constructor ArrayList (arg0: int)
    {
        action TODO();
    }


    // utilities

    // static methods

    // methods

    fun add (arg0: E): boolean
    {
        action TODO();
    }


    fun add (arg0: int, arg1: E): void
    {
        action TODO();
    }


    fun addAll (@Parameterized(["? extends E"]) arg0: Collection): boolean
    {
        action TODO();
    }


    fun addAll (arg0: int, @Parameterized(["? extends E"]) arg1: Collection): boolean
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


    fun contains (arg0: Object): boolean
    {
        action TODO();
    }


    fun ensureCapacity (arg0: int): void
    {
        action TODO();
    }


    fun equals (arg0: Object): boolean
    {
        action TODO();
    }


    fun forEach (@Parameterized(["? super E"]) arg0: Consumer): void
    {
        action TODO();
    }


    fun get (arg0: int): E
    {
        action TODO();
    }


    fun hashCode (): int
    {
        action TODO();
    }


    fun indexOf (arg0: Object): int
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


    fun lastIndexOf (arg0: Object): int
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun listIterator (): ListIterator
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun listIterator (arg0: int): ListIterator
    {
        action TODO();
    }


    fun remove (arg0: Object): boolean
    {
        action TODO();
    }


    fun remove (arg0: int): E
    {
        action TODO();
    }


    fun removeAll (@Parameterized(["?"]) arg0: Collection): boolean
    {
        action TODO();
    }


    fun removeIf (@Parameterized(["? super E"]) arg0: Predicate): boolean
    {
        action TODO();
    }


    fun replaceAll (@Parameterized(["E"]) arg0: UnaryOperator): void
    {
        action TODO();
    }


    fun retainAll (@Parameterized(["?"]) arg0: Collection): boolean
    {
        action TODO();
    }


    fun set (arg0: int, arg1: E): E
    {
        action TODO();
    }


    fun size (): int
    {
        action TODO();
    }


    fun sort (@Parameterized(["? super E"]) arg0: Comparator): void
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun spliterator (): Spliterator
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun subList (arg0: int, arg1: int): List
    {
        action TODO();
    }


    fun toArray (): array<Object>
    {
        action TODO();
    }


    @Parameterized(["T"])
    fun toArray (arg0: array<T>): array<T>
    {
        action TODO();
    }


    fun trimToSize (): void
    {
        action TODO();
    }

}
