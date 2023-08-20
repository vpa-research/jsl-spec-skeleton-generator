libsl "1.1.0";

library "std:???"
    version "11"
    language "Java"
    url "-";

// imports

import "java-common.lsl";
import "java/util/_interfaces.lsl";
import "java/util/function/_interfaces.lsl";

import "list-actions.lsl";


// local semantic types

@TypeMapping(typeVariable=true) typealias K = Object;
@TypeMapping(typeVariable=true) typealias V = Object;


// automata

@Parameterized(["K", "V"])
@extends("java.util.AbstractMap<K, V>")
@implements("java.util.Map<K, V>")
@implements("java.lang.Cloneable")
@implements("java.io.Serializable")
@public automaton HashMap: int
(
    @private @static @final var serialVersionUID: long = 362498820763181265;
)
{
    // states and shifts

    initstate Initialized;


    // constructors

    constructor HashMap ()
    {
        action TODO();
    }


    constructor HashMap (@Parameterized(["? extends K", "? extends V"]) arg0: Map)
    {
        action TODO();
    }


    constructor HashMap (arg0: int)
    {
        action TODO();
    }


    constructor HashMap (arg0: int, arg1: float)
    {
        action TODO();
    }


    // utilities

    // static methods

    // methods

    fun clear (): void
    {
        action TODO();
    }


    fun clone (): Object
    {
        action TODO();
    }


    fun compute (arg0: K, @Parameterized(["? super K", "? super V", "? extends V"]) arg1: BiFunction): V
    {
        action TODO();
    }


    fun computeIfAbsent (arg0: K, @Parameterized(["? super K", "? extends V"]) arg1: Function): V
    {
        action TODO();
    }


    fun computeIfPresent (arg0: K, @Parameterized(["? super K", "? super V", "? extends V"]) arg1: BiFunction): V
    {
        action TODO();
    }


    fun containsKey (arg0: Object): boolean
    {
        action TODO();
    }


    fun containsValue (arg0: Object): boolean
    {
        action TODO();
    }


    @ParameterizedResult(["java.util.Map$Entry<K, V>"])
    fun entrySet (): Set
    {
        action TODO();
    }


    fun forEach (@Parameterized(["? super K", "? super V"]) arg0: BiConsumer): void
    {
        action TODO();
    }


    fun get (arg0: Object): V
    {
        action TODO();
    }


    fun getOrDefault (arg0: Object, arg1: V): V
    {
        action TODO();
    }


    fun isEmpty (): boolean
    {
        action TODO();
    }


    @ParameterizedResult(["K"])
    fun keySet (): Set
    {
        action TODO();
    }


    fun merge (arg0: K, arg1: V, @Parameterized(["? super V", "? super V", "? extends V"]) arg2: BiFunction): V
    {
        action TODO();
    }


    fun put (arg0: K, arg1: V): V
    {
        action TODO();
    }


    fun putAll (@Parameterized(["? extends K", "? extends V"]) arg0: Map): void
    {
        action TODO();
    }


    fun putIfAbsent (arg0: K, arg1: V): V
    {
        action TODO();
    }


    fun remove (arg0: Object): V
    {
        action TODO();
    }


    fun remove (arg0: Object, arg1: Object): boolean
    {
        action TODO();
    }


    fun replace (arg0: K, arg1: V): V
    {
        action TODO();
    }


    fun replace (arg0: K, arg1: V, arg2: V): boolean
    {
        action TODO();
    }


    fun replaceAll (@Parameterized(["? super K", "? super V", "? extends V"]) arg0: BiFunction): void
    {
        action TODO();
    }


    fun size (): int
    {
        action TODO();
    }


    @ParameterizedResult(["V"])
    fun values (): Collection
    {
        action TODO();
    }

}
