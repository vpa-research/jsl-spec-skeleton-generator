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

@For(automaton="ArrayListAutomaton", insteadOf="java.util.ArrayList")
@Parameterized("E")
@extends("java.util.AbstractList<E>")
@implements(["java.util.List<E>", "java.util.RandomAccess", "java.lang.Cloneable", "java.io.Serializable"])
@public type ArrayList
{
    @private @static @final var serialVersionUID: long = 8683452581122892189;
}


// automata

@Parameterized("E")
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


    constructor `ArrayList#ArrayList` (@target obj: ArrayList, @Parameterized("? extends E") arg0: Collection)
    {
        action TODO();
    }


    constructor `ArrayList#ArrayList` (@target obj: ArrayList, arg0: int)
    {
        action TODO();
    }


    // utilities

    // static methods

    // methods

    fun `ArrayList#add` (@target obj: ArrayList, arg0: E): boolean
    {
        action TODO();
    }


    fun `ArrayList#add` (@target obj: ArrayList, arg0: int, arg1: E): void
    {
        action TODO();
    }


    fun `ArrayList#addAll` (@target obj: ArrayList, @Parameterized("? extends E") arg0: Collection): boolean
    {
        action TODO();
    }


    fun `ArrayList#addAll` (@target obj: ArrayList, arg0: int, @Parameterized("? extends E") arg1: Collection): boolean
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


    fun `ArrayList#contains` (@target obj: ArrayList, arg0: Object): boolean
    {
        action TODO();
    }


    fun `ArrayList#ensureCapacity` (@target obj: ArrayList, arg0: int): void
    {
        action TODO();
    }


    fun `ArrayList#equals` (@target obj: ArrayList, arg0: Object): boolean
    {
        action TODO();
    }


    fun `ArrayList#forEach` (@target obj: ArrayList, @Parameterized("? super E") arg0: Consumer): void
    {
        action TODO();
    }


    fun `ArrayList#get` (@target obj: ArrayList, arg0: int): E
    {
        action TODO();
    }


    fun `ArrayList#hashCode` (@target obj: ArrayList): int
    {
        action TODO();
    }


    fun `ArrayList#indexOf` (@target obj: ArrayList, arg0: Object): int
    {
        action TODO();
    }


    fun `ArrayList#isEmpty` (@target obj: ArrayList): boolean
    {
        action TODO();
    }


    @ParameterizedResult("E")
    fun `ArrayList#iterator` (@target obj: ArrayList): Iterator
    {
        action TODO();
    }


    fun `ArrayList#lastIndexOf` (@target obj: ArrayList, arg0: Object): int
    {
        action TODO();
    }


    @ParameterizedResult("E")
    fun `ArrayList#listIterator` (@target obj: ArrayList): ListIterator
    {
        action TODO();
    }


    @ParameterizedResult("E")
    fun `ArrayList#listIterator` (@target obj: ArrayList, arg0: int): ListIterator
    {
        action TODO();
    }


    fun `ArrayList#remove` (@target obj: ArrayList, arg0: Object): boolean
    {
        action TODO();
    }


    fun `ArrayList#remove` (@target obj: ArrayList, arg0: int): E
    {
        action TODO();
    }


    fun `ArrayList#removeAll` (@target obj: ArrayList, @Parameterized("?") arg0: Collection): boolean
    {
        action TODO();
    }


    fun `ArrayList#removeIf` (@target obj: ArrayList, @Parameterized("? super E") arg0: Predicate): boolean
    {
        action TODO();
    }


    fun `ArrayList#replaceAll` (@target obj: ArrayList, @Parameterized("E") arg0: UnaryOperator): void
    {
        action TODO();
    }


    fun `ArrayList#retainAll` (@target obj: ArrayList, @Parameterized("?") arg0: Collection): boolean
    {
        action TODO();
    }


    fun `ArrayList#set` (@target obj: ArrayList, arg0: int, arg1: E): E
    {
        action TODO();
    }


    fun `ArrayList#size` (@target obj: ArrayList): int
    {
        action TODO();
    }


    fun `ArrayList#sort` (@target obj: ArrayList, @Parameterized("? super E") arg0: Comparator): void
    {
        action TODO();
    }


    @ParameterizedResult("E")
    fun `ArrayList#spliterator` (@target obj: ArrayList): Spliterator
    {
        action TODO();
    }


    @ParameterizedResult("E")
    fun `ArrayList#subList` (@target obj: ArrayList, arg0: int, arg1: int): List
    {
        action TODO();
    }


    fun `ArrayList#toArray` (@target obj: ArrayList): array<Object>
    {
        action TODO();
    }


    @Parameterized("T")
    fun `ArrayList#toArray` (@target obj: ArrayList, arg0: array<T>): array<T>
    {
        action TODO();
    }


    fun `ArrayList#trimToSize` (@target obj: ArrayList): void
    {
        action TODO();
    }

}
