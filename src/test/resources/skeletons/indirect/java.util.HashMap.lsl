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

@For(automaton="HashMapAutomaton", insteadOf="java.util.HashMap")
@Parameterized("K, V")
@extends("java.util.AbstractMap<K, V>")
@implements(["java.util.Map<K, V>", "java.lang.Cloneable", "java.io.Serializable"])
@public type HashMap
{
    @private @static @final var serialVersionUID: long = 362498820763181265;
}


// automata

@Parameterized("K, V")
@public automaton HashMapAutomaton: HashMap
(
)
{
    // states and shifts

    initstate Initialized;


    // constructors

    constructor `HashMap#HashMap` (@target obj: HashMap)
    {
        action TODO();
    }


    constructor `HashMap#HashMap` (@target obj: HashMap, @Parameterized("? extends K, ? extends V") arg0: Map)
    {
        action TODO();
    }


    constructor `HashMap#HashMap` (@target obj: HashMap, arg0: int)
    {
        action TODO();
    }


    constructor `HashMap#HashMap` (@target obj: HashMap, arg0: int, arg1: float)
    {
        action TODO();
    }


    // utilities

    // static methods

    // methods

    fun `HashMap#clear` (@target obj: HashMap): void
    {
        action TODO();
    }


    fun `HashMap#clone` (@target obj: HashMap): Object
    {
        action TODO();
    }


    fun `HashMap#compute` (@target obj: HashMap, arg0: K, @Parameterized("? super K, ? super V, ? extends V") arg1: BiFunction): V
    {
        action TODO();
    }


    fun `HashMap#computeIfAbsent` (@target obj: HashMap, arg0: K, @Parameterized("? super K, ? extends V") arg1: Function): V
    {
        action TODO();
    }


    fun `HashMap#computeIfPresent` (@target obj: HashMap, arg0: K, @Parameterized("? super K, ? super V, ? extends V") arg1: BiFunction): V
    {
        action TODO();
    }


    fun `HashMap#containsKey` (@target obj: HashMap, arg0: Object): boolean
    {
        action TODO();
    }


    fun `HashMap#containsValue` (@target obj: HashMap, arg0: Object): boolean
    {
        action TODO();
    }


    @ParameterizedResult("java.util.Map$Entry<K, V>")
    fun `HashMap#entrySet` (@target obj: HashMap): Set
    {
        action TODO();
    }


    fun `HashMap#forEach` (@target obj: HashMap, @Parameterized("? super K, ? super V") arg0: BiConsumer): void
    {
        action TODO();
    }


    fun `HashMap#get` (@target obj: HashMap, arg0: Object): V
    {
        action TODO();
    }


    fun `HashMap#getOrDefault` (@target obj: HashMap, arg0: Object, arg1: V): V
    {
        action TODO();
    }


    fun `HashMap#isEmpty` (@target obj: HashMap): boolean
    {
        action TODO();
    }


    @ParameterizedResult("K")
    fun `HashMap#keySet` (@target obj: HashMap): Set
    {
        action TODO();
    }


    fun `HashMap#merge` (@target obj: HashMap, arg0: K, arg1: V, @Parameterized("? super V, ? super V, ? extends V") arg2: BiFunction): V
    {
        action TODO();
    }


    fun `HashMap#put` (@target obj: HashMap, arg0: K, arg1: V): V
    {
        action TODO();
    }


    fun `HashMap#putAll` (@target obj: HashMap, @Parameterized("? extends K, ? extends V") arg0: Map): void
    {
        action TODO();
    }


    fun `HashMap#putIfAbsent` (@target obj: HashMap, arg0: K, arg1: V): V
    {
        action TODO();
    }


    fun `HashMap#remove` (@target obj: HashMap, arg0: Object): V
    {
        action TODO();
    }


    fun `HashMap#remove` (@target obj: HashMap, arg0: Object, arg1: Object): boolean
    {
        action TODO();
    }


    fun `HashMap#replace` (@target obj: HashMap, arg0: K, arg1: V): V
    {
        action TODO();
    }


    fun `HashMap#replace` (@target obj: HashMap, arg0: K, arg1: V, arg2: V): boolean
    {
        action TODO();
    }


    fun `HashMap#replaceAll` (@target obj: HashMap, @Parameterized("? super K, ? super V, ? extends V") arg0: BiFunction): void
    {
        action TODO();
    }


    fun `HashMap#size` (@target obj: HashMap): int
    {
        action TODO();
    }


    @ParameterizedResult("V")
    fun `HashMap#values` (@target obj: HashMap): Collection
    {
        action TODO();
    }

}
