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
import "java/util/AbstractList.lsl";
import "java/util/Collection.lsl";
import "java/util/Comparator.lsl";
import "java/util/Iterator.lsl";
import "java/util/List.lsl";
import "java/util/ListIterator.lsl";
import "java/util/RandomAccess.lsl";
import "java/util/Spliterator.lsl";
import "java/util/function/Consumer.lsl";
import "java/util/function/Predicate.lsl";
import "java/util/function/UnaryOperator.lsl";

import "list-actions.lsl";


// local semantic types

@TypeMapping(typeVariable=true) typealias E = Object;

@For(automaton="ArrayListAutomaton", insteadOf="java.util.ArrayList")
@Parameterized(["E"])
@extends("java.util.AbstractList<E>")
@implements("java.util.List<E>")
@implements("java.util.RandomAccess")
@implements("java.lang.Cloneable")
@implements("java.io.Serializable")
@public type ArrayList
{
    @private @static @final var serialVersionUID: long = 8683452581122892189;
}


// automata

@Parameterized(["E"])
@public automaton ArrayListAutomaton: ArrayList
(
)
{
    // states and shifts

    initstate Initialized;


    // constructors

    constructor `ArrayList#ArrayList` (@target obj: ArrayList)
    {
        action TODO();
    }


    constructor `ArrayList#ArrayList` (@target obj: ArrayList, @Parameterized(["? extends E"]) c: Collection)
    {
        action TODO();
    }


    constructor `ArrayList#ArrayList` (@target obj: ArrayList, initialCapacity: int)
    {
        action TODO();
    }


    // utilities

    // static methods

    // methods

    fun `ArrayList#add` (@target obj: ArrayList, e: E): boolean
    {
        action TODO();
    }


    fun `ArrayList#add` (@target obj: ArrayList, index: int, element: E): void
    {
        action TODO();
    }


    fun `ArrayList#addAll` (@target obj: ArrayList, @Parameterized(["? extends E"]) c: Collection): boolean
    {
        action TODO();
    }


    fun `ArrayList#addAll` (@target obj: ArrayList, index: int, @Parameterized(["? extends E"]) c: Collection): boolean
    {
        action TODO();
    }


    fun `ArrayList#clear` (@target obj: ArrayList): void
    {
        action TODO();
    }


    fun `ArrayList#clone` (@target obj: ArrayList): Object
    {
        action TODO();
    }


    fun `ArrayList#contains` (@target obj: ArrayList, o: Object): boolean
    {
        action TODO();
    }


    fun `ArrayList#ensureCapacity` (@target obj: ArrayList, minCapacity: int): void
    {
        action TODO();
    }


    fun `ArrayList#equals` (@target obj: ArrayList, o: Object): boolean
    {
        action TODO();
    }


    fun `ArrayList#forEach` (@target obj: ArrayList, @Parameterized(["? super E"]) _action: Consumer): void
    {
        action TODO();
    }


    fun `ArrayList#get` (@target obj: ArrayList, index: int): E
    {
        action TODO();
    }


    fun `ArrayList#hashCode` (@target obj: ArrayList): int
    {
        action TODO();
    }


    fun `ArrayList#indexOf` (@target obj: ArrayList, o: Object): int
    {
        action TODO();
    }


    fun `ArrayList#isEmpty` (@target obj: ArrayList): boolean
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun `ArrayList#iterator` (@target obj: ArrayList): Iterator
    {
        action TODO();
    }


    fun `ArrayList#lastIndexOf` (@target obj: ArrayList, o: Object): int
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun `ArrayList#listIterator` (@target obj: ArrayList): ListIterator
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun `ArrayList#listIterator` (@target obj: ArrayList, index: int): ListIterator
    {
        action TODO();
    }


    fun `ArrayList#remove` (@target obj: ArrayList, o: Object): boolean
    {
        action TODO();
    }


    fun `ArrayList#remove` (@target obj: ArrayList, index: int): E
    {
        action TODO();
    }


    fun `ArrayList#removeAll` (@target obj: ArrayList, @Parameterized(["?"]) c: Collection): boolean
    {
        action TODO();
    }


    fun `ArrayList#removeIf` (@target obj: ArrayList, @Parameterized(["? super E"]) filter: Predicate): boolean
    {
        action TODO();
    }


    fun `ArrayList#replaceAll` (@target obj: ArrayList, @Parameterized(["E"]) operator: UnaryOperator): void
    {
        action TODO();
    }


    fun `ArrayList#retainAll` (@target obj: ArrayList, @Parameterized(["?"]) c: Collection): boolean
    {
        action TODO();
    }


    fun `ArrayList#set` (@target obj: ArrayList, index: int, element: E): E
    {
        action TODO();
    }


    fun `ArrayList#size` (@target obj: ArrayList): int
    {
        action TODO();
    }


    fun `ArrayList#sort` (@target obj: ArrayList, @Parameterized(["? super E"]) c: Comparator): void
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun `ArrayList#spliterator` (@target obj: ArrayList): Spliterator
    {
        action TODO();
    }


    @ParameterizedResult(["E"])
    fun `ArrayList#subList` (@target obj: ArrayList, fromIndex: int, toIndex: int): List
    {
        action TODO();
    }


    fun `ArrayList#toArray` (@target obj: ArrayList): array<Object>
    {
        action TODO();
    }


    @Parameterized(["T"])
    fun `ArrayList#toArray` (@target obj: ArrayList, a: array<T>): array<T>
    {
        action TODO();
    }


    fun `ArrayList#trimToSize` (@target obj: ArrayList): void
    {
        action TODO();
    }

}
