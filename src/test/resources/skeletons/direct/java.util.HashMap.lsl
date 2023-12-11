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
import "java/util/AbstractMap.lsl";
import "java/util/Collection.lsl";
import "java/util/Map.lsl";
import "java/util/Set.lsl";
import "java/util/function/BiConsumer.lsl";
import "java/util/function/BiFunction.lsl";
import "java/util/function/Function.lsl";

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

    constructor `<init>` ()
    {
        action TODO();
    }


    constructor `<init>` (@Parameterized(["? extends K", "? extends V"]) m: Map)
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


    fun compute (key: K, @Parameterized(["? super K", "? super V", "? extends V"]) remappingFunction: BiFunction): V
    {
        action TODO();
    }


    fun computeIfAbsent (key: K, @Parameterized(["? super K", "? extends V"]) mappingFunction: Function): V
    {
        action TODO();
    }


    fun computeIfPresent (key: K, @Parameterized(["? super K", "? super V", "? extends V"]) remappingFunction: BiFunction): V
    {
        action TODO();
    }


    fun containsKey (key: Object): boolean
    {
        action TODO();
    }


    fun containsValue (value: Object): boolean
    {
        action TODO();
    }


    @ParameterizedResult(["java.util.Map$Entry<K, V>"])
    fun entrySet (): Set
    {
        action TODO();
    }


    fun forEach (@Parameterized(["? super K", "? super V"]) _action: BiConsumer): void
    {
        action TODO();
    }


    fun get (key: Object): V
    {
        action TODO();
    }


    fun getOrDefault (key: Object, defaultValue: V): V
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


    fun merge (key: K, value: V, @Parameterized(["? super V", "? super V", "? extends V"]) remappingFunction: BiFunction): V
    {
        action TODO();
    }


    fun put (key: K, value: V): V
    {
        action TODO();
    }


    fun putAll (@Parameterized(["? extends K", "? extends V"]) m: Map): void
    {
        action TODO();
    }


    fun putIfAbsent (key: K, value: V): V
    {
        action TODO();
    }


    fun remove (key: Object): V
    {
        action TODO();
    }


    fun remove (key: Object, value: Object): boolean
    {
        action TODO();
    }


    fun replace (key: K, value: V): V
    {
        action TODO();
    }


    fun replace (key: K, oldValue: V, newValue: V): boolean
    {
        action TODO();
    }


    fun replaceAll (@Parameterized(["? super K", "? super V", "? extends V"]) function: BiFunction): void
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
