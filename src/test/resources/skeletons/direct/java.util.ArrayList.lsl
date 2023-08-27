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


    constructor ArrayList (@Parameterized(["? extends E"]) c: Collection)
    {
        action TODO();
    }


    constructor ArrayList (initialCapacity: int)
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


    fun add (index: int, element: E): void
    {
        action TODO();
    }


    fun addAll (@Parameterized(["? extends E"]) c: Collection): boolean
    {
        action TODO();
    }


    fun addAll (index: int, @Parameterized(["? extends E"]) c: Collection): boolean
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


    fun ensureCapacity (minCapacity: int): void
    {
        action TODO();
    }


    fun equals (o: Object): boolean
    {
        action TODO();
    }


    fun forEach (@Parameterized(["? super E"]) _action: Consumer): void
    {
        action TODO();
    }


    fun get (index: int): E
    {
        action TODO();
    }


    fun hashCode (): int
    {
        action TODO();
    }


    fun indexOf (o: Object): int
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


    fun lastIndexOf (o: Object): int
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun listIterator (): ListIterator
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun listIterator (index: int): ListIterator
    {
        action TODO();
    }


    fun remove (o: Object): boolean
    {
        action TODO();
    }


    fun remove (index: int): E
    {
        action TODO();
    }


    fun removeAll (@Parameterized(["?"]) c: Collection): boolean
    {
        action TODO();
    }


    fun removeIf (@Parameterized(["? super E"]) filter: Predicate): boolean
    {
        action TODO();
    }


    fun replaceAll (@Parameterized(["E"]) operator: UnaryOperator): void
    {
        action TODO();
    }


    fun retainAll (@Parameterized(["?"]) c: Collection): boolean
    {
        action TODO();
    }


    fun set (index: int, element: E): E
    {
        action TODO();
    }


    fun size (): int
    {
        action TODO();
    }


    fun sort (@Parameterized(["? super E"]) c: Comparator): void
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun spliterator (): Spliterator
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun subList (fromIndex: int, toIndex: int): List
    {
        action TODO();
    }


    fun toArray (): array<Object>
    {
        action TODO();
    }


    @Parameterized(["T"])
    fun toArray (a: array<T>): array<T>
    {
        action TODO();
    }


    fun trimToSize (): void
    {
        action TODO();
    }

}
